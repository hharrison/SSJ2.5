#!/bin/perl

# This script manage the ~/.latex2html-init LATEX2HTMLSTYLES variable.
# Its first argument must be a path that will be looked for in the LATEX2HTMLSTYLES
# variable. If the path is absent, it will be added.

use File::Spec;

if ($^O eq 'MSWin32' || $^O =~ /dos|win/) {
   $pathsep = ';';
}
else {
   $pathsep = ':';
}


if (@ARGV != 1) {
   print "Usage: perl setl2hinit.pl <directory to be added to LATEX2HTMLSTYLES variable>\n";
   exit 1;
}
$ARGV[0] = File::Spec->canonpath ($ARGV[0]);

die "$ARGV[0] directory does not exist" if !-x $ARGV[0];
die "HOME environment variable not set" if !$ENV{'HOME'};

$l2hinit = File::Spec->catfile ($ENV{'HOME'}, '.latex2html-init');

if (!-r $l2hinit) {
   # Create the file if it does not exist yet.
   open L2H, ">$l2hinit" or die "Cannot create $l2hinit";
   print L2H '$LATEX2HTMLSTYLES .= "' . quotepath ($pathsep . $ARGV[0]) . "\";\n";
   printf L2H "\n1;\n";
   close L2H;
   exit 0;
}

# Read the file (and the LATEX2HTMLSTYLES variable)
require ($l2hinit);

@p = split /$pathsep/, $LATEX2HTMLSTYLES;
foreach my $dir (@p) {
   $dir = File::Spec->canonpath ($dir);
   if ($dir eq $ARGV[0]) {
      # The path was found; nothing to be done
      exit 0;
   }
}

# Since the directory was not found, it must be added.
# However, the rest of the file must not be altered.
# The safest way to do it is to add a new line to the init file.

open L2H, "<$l2hinit" or die "Cannot read $l2hinit";
$contents = join "", <L2H>;
close L2H;

$contents =~ s/\s*1;\s*$//;
$contents .= "\n\$LATEX2HTMLSTYLES .= \"" . quotepath ($pathsep . $ARGV[0])
  . "\";\n\n1;\n";

open L2H, ">$l2hinit" or die "Cannot write to $l2hinit";
print L2H $contents;
close L2H;

sub quotepath {
   # Quotes the backslashes in paths
   my $p = shift;

   $p =~ s/\\/\\\\/go;
   return $p;
}

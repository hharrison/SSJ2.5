&do_require_package ('amsfonts');
&do_require_package ('alltt');
&do_require_package ('html');
&do_require_package ('tcode');

sub do_math_cmd_g {
   (" &lt;-- ", @_);
}

# sub do_math_cmd_tbu {
#    ("<B>&utilde;</B>", @_);
# }

sub do_math_cmd_bu {
   ("<B>u</B>", @_);
}

sub do_math_cmd_ba {
    ("<B>a</B>", @_);
}

sub do_math_cmd_be {
    ("<B>e</B>", @_);
}

sub do_math_cmd_bg {
    ("<B>g</B>", @_);
}

sub do_math_cmd_bq {
    ("<B>q</B>", @_);
}

sub do_math_cmd_bv {
    ("<B>v</B>", @_);
}

sub do_math_cmd_bA {
    ("<B>A</B>", @_);
}

sub do_math_cmd_bB {
    ("<B>B</B>", @_);
}

sub do_math_cmd_bC {
    ("<B>C</B>", @_);
}

sub do_math_cmd_bM {
    ("<B>M</B>", @_);
}

sub do_math_cmd_bP {
    ("<B>P</B>", @_);
}

sub do_math_cmd_bU {
    ("<B>U</B>", @_);
}

sub do_math_cmd_bV {
    ("<B>V</B>", @_);
}

sub do_math_cmd_bX {
    ("<B>X</B>", @_);
}

sub do_math_cmd_bR {
    ("<B>R</B>", @_);
}

sub do_math_cmd_bS {
    ("<B>S</B>", @_);
}

sub do_math_cmd_bZ {
    ("<B>Z</B>", @_);
}

sub do_math_cmd_bI {
    ("<B>I</B>", @_);
}

sub do_math_cmd_bzero {
    ("<B>0</B>", @_);
}

sub do_math_cmd_cS {
    ("S", @_);
}

sub do_math_cmd_ZZ {
    ("<B>Z</B>", @_);
}

sub do_math_cmd_RR {
    ("<B>R</B>", @_);
}

sub do_math_cmd_NN {
    ("<B>N</B>", @_);
}

sub do_math_cmd_FF {
    ("<B>F</B>", @_);
}

sub do_math_cmd_Ki {
    ("<I>K</I><SUB>I</SUB>", @_);
}

sub do_math_cmd_Ko {
    ("<I>K</I><SUB>O</SUB>", @_);
}

sub do_math_cmd_RR {
    ("<B>R</B>", @_);
}

sub do_math_cmd_NN {
    ("<B>N</B>", @_);
}

sub do_math_cmd_boldmu {
    ("<I><B>&mu;</B></I>", @_);
}

sub do_math_cmd_boldnu {
    ("<I><B>&nu;</B></I>", @_);
}

sub do_math_cmd_boldbeta {
    ("<I><B>&beta;</B></I>", @_);
}

sub do_math_cmd_boldbetaf {
    ("<I><B>&beta;</B></I><SUB>f</SUB>", @_);
}

sub do_math_cmd_betaf {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>&beta;</I><SUB>f" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_barboldmu {
    ("bar(<I><B>&mu;</B></I>)", @_);
}

sub do_math_cmd_barboldnu {
    ("bar(<I><B>&mu;</B></I>)", @_);
}

sub do_math_cmd_barboldX {
    ("bar(<I><B>X</B></I>)", @_);
}

sub do_math_cmd_barboldx {
    ("bar(<I><B>x</B></I>)", @_);
}

sub do_math_cmd_boldSigma {
    ("<I><B>&Sigma;</B></I>", @_);
}

sub do_math_cmd_bsigma {
    ("<I><B>&sigma;</B></I>", @_);
}

sub do_math_cmd_boldLambda {
    ("<I><B>&Lambda;</B></I>", @_);
}

sub do_math_cmd_boldC {
    ("<I><B>C</B></I>", @_);
}

sub do_math_cmd_boldX {
    ("<I><B>X</B></I>", @_);
}

sub do_math_cmd_boldY {
    ("<I><B>Y</B></I>", @_);
}

sub do_math_cmd_boldV {
    ("<B><I>V</I></B>", @_);
}

sub do_math_cmd_boldS {
    ("<B><I>S</I></B>", @_);
}

sub do_math_cmd_boldf {
    ("<I><B>f</B></I>", @_);
}

sub do_math_cmd_rTG {
    ("<I>r</I><SUB>TG</SUB>", @_);
}

sub do_math_cmd_rGT {
    ("<I>r</I><SUB>GT</SUB>", @_);
}

sub do_math_cmd_Nf {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>N</I><SUB>f" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_Nb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>N</I><SUB>b" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_Ni {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>N</I><SUB>i" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_Ng {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>N</I><SUB>g" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_Ntb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>N</I><SUB>b" . ($n ? "<I>, $n</I>" : "") . "</SUB><SUP>t</SUP>", $_);
}

sub do_math_cmd_Ntf {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>N</I><SUB>f" . ($n ? "<I>, $n</I>" : "") . "</SUB><SUP>t</SUP>", $_);
}

sub do_math_cmd_Ndf {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>N</I><SUB>f" . ($n ? "<I>, $n</I>" : "") . "</SUB><SUP>d</SUP>", $_);
}

sub do_math_cmd_Xb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>X</I><SUB>b" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_barXb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("bar(<I>X</I><SUB>b" . ($n ? "<I>, $n</I>" : "") . "</SUB>)", $_);
}

sub do_math_cmd_XbK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>X</I><SUB>b<I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_XbP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>X</I><SUB>b<I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_Xg {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>X</I><SUB>g" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_barXg {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("bar(<I>X</I><SUB>g" . ($n ? "<I>, $n</I>" : "") . "</SUB>)", $_);
}

sub do_math_cmd_XgK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>X</I><SUB>g<I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_XgP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>X</I><SUB>g<I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_Yb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>Y</I><SUB>b" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_barYb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("bar(<I>Y</I><SUB>b" . ($n ? "<I>, $n</I>" : "") . "</SUB>)", $_);
}

sub do_math_cmd_YbK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>Y</I><SUB>b<I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_YbP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>Y</I><SUB>b<I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_Yg {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>Y</I><SUB>g" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_barYg {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("bar(<I>Y</I><SUB>g" . ($n ? "<I>, $n</I>" : "") . "</SUB>)", $_);
}

sub do_math_cmd_YgK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>Y</I><SUB>g<I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_YgP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>Y</I><SUB>g<I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_Var {
    ("Var", @_);
}

sub do_math_cmd_Cov {
    ("Cov", @_);
}

sub do_math_cmd_sK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>s</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_sP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>s</I><SUB><I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_lK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>l</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_lP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_next_token;
    ("<I>l</I><SUB><I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_XK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>X</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_XP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>X</I><SUB><I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_YK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>Y</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_YP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>Y</I><SUB><I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_BK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>B</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_BP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>B</I><SUB><I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_AK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>A</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_AP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>A</I><SUB><I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_WK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>W</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_WP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>W</I><SUB><I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_WX {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>W</I><SUB>X" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_WXK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>W</I><SUB>X<I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_WXP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>W</I><SUB>X<I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_WY {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>W</I><SUB>Y" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_WYK {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>W</I><SUB>Y<I>, $n, ." . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_WYP {
    local($_) = @_;
    local($m) = &get_next_optional_argument;
    local($n) = &get_token;
    ("<I>W</I><SUB>Y<I>, ., $n" . ($m ? ", $m" : "") . "</I></SUB>", $_);
}

sub do_math_cmd_Ntb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>X</I><SUB>C" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_Ntb {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I><B>X</B></I><SUB>C" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_XC {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I>X</I><SUB>C" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_boldXC {
    local($_) = @_;
    local($n) = &get_next_optional_argument;
    ("<I><B>X</B></I><SUB>C" . ($n ? "<I>, $n</I>" : "") . "</SUB>", $_);
}

sub do_math_cmd_rmc {
    ("c", @_);
}

sub do_math_cmd_rmC {
    ("C", @_);
}

sub do_math_cmd_rmCX {
    ("CX", @_);
}

sub do_math_cmd_rmX {
    ("X", @_);
}

sub do_math_cmd_tr {
    ("t", @_);
}

sub do_math_cmd_wTG {
    ("<I>w</I><SUB>TG</SUB>", @_);
}

sub do_math_cmd_wGT {
    ("<I>w</I><SUB>GT</SUB>", @_);
}

&ignore_commands ("boxnote # {} # {}\npierre # {}\nhpierre # {}\n"
       . "adam # {}\nhadam # {}\n"
       . "richard # {}\nhrichard # {}\n"
       . "eric # {}\nheric # {}\n"
       . "pierre # {}\nhpierre # {}");

1;

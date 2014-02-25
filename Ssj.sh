#!/bin/sh

export SSJHOME=<SSJ home path>/ssj-2

if [ ! $SSJHOME ]; then
    echo "You must set SSJHOME before calling this script."
    exit
fi


case "`uname -m`" in
   'i686' )
       export LD_LIBRARY_PATH=$SSJHOME/lib:$LD_LIBRARY_PATH ;;
   'x86_64' )
       export LD_LIBRARY_PATH=$SSJHOME/lib64:$LD_LIBRARY_PATH ;;
esac

export CLASSPATH=.:$SSJHOME/lib/ssj.jar:$SSJHOME/lib/colt.jar:$SSJHOME/lib/tcode.jar:$CLASSPATH

export SSJSRC=$SSJHOME/source/umontreal/iro/lecuyer
export TEXINPUTS=.:$SSJHOME/source:$PGFLIBRARY//:$TEXINPUTS

function cdssj() {
   cd $SSJHOME/$1
}

function cdssjsrc() {
   cd $SSJHOME/source/umontreal/iro/lecuyer/$1
}

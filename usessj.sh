#!/bin/sh

export SSJHOME=<SSJ home path>/ssj-2

if [ ! $SSJHOME ]; then
    echo "You must set SSJHOME before calling this script."
    exit
fi
if [ ! $CLASSPATH ]; then
    export CLASSPATH=
fi
if [ ! $LD_LIBRARY_PATH ]; then
    export LD_LIBRARY_PATH=
fi

export CLASSPATH=.:$SSJHOME/lib/ssj.jar:$SSJHOME/lib/colt.jar:$SSJHOME/lib/tcode.jar:$CLASSPATH

case "`uname -m`" in
   'i686' )
       export LD_LIBRARY_PATH=$SSJHOME/lib:$LD_LIBRARY_PATH ;;
   'x86_64' )
       export LD_LIBRARY_PATH=$SSJHOME/lib64:$LD_LIBRARY_PATH ;;
esac

function cdssj() {
   cd $SSJHOME/$1
}

function cdssjsrc() {
   cd $SSJHOME/source/umontreal/iro/lecuyer/$1
}

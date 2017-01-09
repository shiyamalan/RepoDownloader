#!/bin/bash

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
	echo "Usage:"
	echo "git-svn-partial-clone.sh <working copy name> <svn module root> <svn branch pattern>"
    echo "working copy name     : The name of the working copy folder"
    echo "svn module root       : The root svn module directory where parent folder of trunk, branches and tags"
    echo "svn branch pattern    : The branch names comma seperated"
    echo "                        Ex: dev,rel,int or trunk,dev"
	exit 1
fi

url=$2
if [[ ! $2 == *./ ]] ; then
    url=$2'/'
fi





rootpath=$1
if [[ ! $1 == '*.\' ]] ; then
    rootpath=$1'\'
fi

if [ -d $rootpath$3'\''.git' ]; then
    echo "A git repository $3 already exist. Exiting ..."
    exit 1
fi

# init empty repository
git svn init $url $rootpath$3
cd $rootpath$3

# configure svn root path
git config --unset-all svn-remote.svn.fetch

branches=$(echo $3 | tr "," "\n")
minrev=0
#for branch in $branches
#do
branch=$3
    if [ $branch == "trunk" ]; then
        bfqn=$branch
    else
        bfqn=branches/$branch
    fi

    brref=$bfqn:refs/remotes/svn/svn-$branch
    git config --add svn-remote.svn.fetch $brref
    
    rev=`svn info -r{"2007-04-17"} $url\$bfqn | grep Revision | cut -d' ' -f2`
	#`svn log  --stop-on-copy $url\$bfqn | gawk '{print $1}' | grep "^r" | tail --lines=1`
	#`svn info -r{"2007-04-17"} $url\$bfqn | grep Revision | cut -d' ' -f2`
	#`svn log --stop-on-copy $url\$bfqn | gawk '{print $1}' | grep "^r" | tail --lines=1`
    rev=${rev:1}

    echo "min revision: $minrev"
    echo "revision [$bfqn]: $rev"
    
    if [ "$minrev" -gt "$rev" ] || [ "$minrev" -eq 0 ]; then 
        minrev=$rev
    fi 
#done
echo "Configuration created"

echo "Fetching starting from revision : $minrev"

echo java2014## | git svn --username SRatnavel ...git svn fetch  -r$minrev:HEAD
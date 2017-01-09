#!/bin/bash
if [ -z "$1" ] || [ -z "$2" ]; then
	echo "Usage:"
	echo "git-svn-partial-clone.sh <svn module root> <svn branch pattern>"
    #echo "working copy name     : The name of the working copy folder"
    echo "svn module root       : The root svn module directory where parent folder of trunk, branches and tags"
    echo "svn branch pattern    : The branch names comma seperated"
    echo "                        Ex: dev,rel,int or trunk,dev"
	exit 1
fi

url=$1
if [[ ! $1 == *./ ]] ; then
    url=$1'/'
fi

# init empty repository
#git svn init $url ./$1
#cd $1

# configure svn root path
#git config --unset-all svn-remote.svn.fetch

branches=$(echo $2 | tr "," "\n")
minrev=0
reponame=$2
for branch in $branches
do	
	if [ -d "./$reponame/.git" ]; then
		echo "A git repository already exist. remove that repository name $branch....."
		#continue
	fi
    if [ $branch == "trunk" ]; then
        bfqn=$branch
    else
        bfqn=branches/$branch
    fi

    brref=$bfqn:refs/remotes/svn/svn-$branch
    git config --add svn-remote.svn.fetch $brref
    
    rev=`svn log --stop-on-copy $url\$bfqn | gawk '{print $branch}' | grep "^r" | tail --lines=1`
	echo "Revision:-"$rev
    rev=${rev:1}

    echo "min revision: $minrev"
    echo "revision [$bfqn]: $rev"
    
    if [ "$minrev" -gt "$rev" ] || [ "$minrev" -eq 0 ]; then 
        minrev=$rev
    fi 
done

# init empty repository
git svn init $url ./$reponame
cd $reponame

# configure svn root path
git config --unset-all svn-remote.svn.fetch

echo "Configuration created"

echo "Fetching starting from revision : $minrev"

git svn fetch -r$minrev:HEAD
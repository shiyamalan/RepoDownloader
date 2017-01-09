#!/bin/bash
#user:RS

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ -z "$4" ]; then
	echo "Usage:"
	echo "git-svn-partial-clone.sh <working copy name> <svn module root> <svn branch pattern>"
    echo "working copy name        : The name of the working copy folder"
    echo "svn module root          : The root svn module directory where parent folder of trunk, branches and tags"
    echo "svn branch pattern       : The branch names comma seperated"
    echo "                         Ex: dev,rel,int or trunk,dev"
	echo "svn repository start date: from when you want download 2014-04-02"
	exit 1
fi

url=$2
repos_status="not downloaded"
repository_status_file_name="status.properties"
repository_status_file=""
if [[ ! $2 == *./ ]] ; then
    url=$2'/'
fi

rootpath=$1
if [[ ! $1 == '*.\' ]] ; then
    rootpath=$1'\'
fi
repository_status_file=$rootpath$repository_status_file_name
if [ -d $rootpath$3'\''.git' ]; then
    echo "A git repository $3 already exist. Exiting ..."
	repos_status="Already exists or Downloaded Previously"
	
	echo $3'='$repos_status >> $repository_status_file
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
    
    #rev=`svn log --stop-on-copy $url\$bfqn | gawk '{print $1}' | grep "^r" | tail --lines=1`
	#`svn log --stop-on-copy "https://subversion.cambio.se/PC/Standard/BlockModuleClient/branches/R8.1_BlockModuleClient_Dev/" | gawk '{print $1}' | grep "^r" | tail --lines=1`
	#`svn info -r{2007-04-17} "https://subversion.cambio.se/PC/Standard/BlockModuleClient/branches/R8.1_BlockModuleClient_Dev/" | grep Revision | cut -d' ' -f2`
	#`svn info -r{"2007-04-17"} | grep Revision | cut -d' ' -f2` 
	#rev=${rev:1}
	
	#fetch the whole log for a prticular repo url
	logs=`svn log --username SRatnavel --password java2014@# $url\$bfqn | grep "^r" | tail --lines=1`
	#echo "Logs are "$logs
	logs_elmnts=$(echo $logs | tr "|" "\n")
	#tLen=${#logs_elmnts[@]}
	index=0
	#if [ $tLen > $((index-1)) ]; then
	lowest_date=""
	#fi
	
	for elmnt in $logs_elmnts
	do
		echo "Element of Logs are: $index------"$elmnt
		if [ $index == 2 ]; then
			lowest_date=$elmnt #get the lowest date for that repo
		fi
		index=$((index+1))
	done
	#echo "Date-----------------"$lowest_date
	
	
	
	dates=$(echo $4 | tr "," "\n")
	index=0
	for elmnt in $dates
	do
	#iterate over 10 dates and find out the possible revision number
		#echo "Element of Logs are: $index------"$elmnt
		start_date=$elmnt #fetching given start date
			
		echo "------ trying new start date "$start_date"-------"	
		#compare  lowest date and given start date
		
		lowstdate=$(date -d $lowest_date +"%Y%m%d")  # = 20130718
		gvndate=$(date -d $start_date +"%Y%m%d")    # = 20130715
		NOW=$(date +"%Y-%m-%d") #current date
	
		end_date=$NOW
	
		if [ $lowstdate -ge $gvndate ]; then #compare the two date
			#echo 'yes';
			start_date=$(date -d $start_date +"%Y-%m-%d") + ""
		fi
		
		echo "------ trying to find the revision ID from "$start_date" to "$end_date" -------"
		#get the log for particular range and get the initial revision number
		rev=`svn log -r {$start_date}:{$end_date} $url\$bfqn | gawk '{print $1}' | grep "^r" | head --lines=1` 
		
		if [ "$rev" == "" ];then
			continue
		else
			break
		fi
	done
			
	echo "Found Revision ID for Start Date:- "$start_date" and End Date:-   "$end_date #fetcing start date
	
	rev=${rev:1} #remove the r from revision number r1789555 -> 1789555
	

    echo "min revision: $minrev"
    echo "revision [$bfqn]: $rev"
    
	if [ "$rev" == "" ];then
	    $repos_status="Error in fetching Revision ID"
		echo $3'='$repos_status >> $repository_status_file
	fi
    if [ "$minrev" -gt "$rev" ] || [ "$minrev" -eq 0 ]; then 
        minrev=$rev
    fi 
#done
echo "Configuration created"

echo "Fetching starting from revision : $minrev"

echo -n 'java2014@#' | git svn fetch --username SRatnavel -r$minrev:HEAD #fetch the repo from minrevision id to latest updates(HEAD)

newlogs=git svn log | grep "^r" | tail --lines=1
if("$logs" == "&newlogs");then
	$repos_status="Successfully Downloaded"
else
	$repos_status="Partially Downloaded. git svn log differ for this repository. check manually"
fi
echo $3'='$repos_status >> $repository_status_file
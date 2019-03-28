#!/bin/bash

$results = $1

for ALG in CoarseGrainListBasedSet HandsOverHandsSet OptimisticSet;
do
    for nbThreads in 1 4 8 12;
    do
	for listSize in 100 1000 10000;
	do
	    for updateRatio in 0 10 100;
	    do
		echo "processing $ALG with nbThreads=$nbThreads, listSize=$listSize, updateRatio=$updateRatio"
		ssh ablicq@lame10.enst.fr "java -cp synchrobench/java/bin contention.benchmark.Test -b linkedlists.lockbased.$ALG -t $nbThreads -u $updateRatio -i $listSize -W 0 -d 2000 >> $results/$ALG-$nbThreads-$listSize-$updateRatio-results.txt"
		echo done
	    done
	done
    done
done

echo test finished

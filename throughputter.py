import subprocess
import os
import re

os.chdir('synchrobench/java')

successNumber = 0
#for ALG in ['CoarseGrainedListBasedSet', 'HandsOverHandsSet', 'OptimisticSet']:
for ALG in ['LazyLinkedListSortedSet']:
    for nbThreads in ['1', '4', '8', '12']:
        for listSize in ['1000']:
            for updateRatio in ['0', '10', '100']:
                try:
                    command = 'java -cp bin contention.benchmark.Test -b linkedlists.lockbased.' \
                            + ALG + ' -t ' + nbThreads + ' -u ' + listSize + ' -i ' \
                            + updateRatio + ' -W 0 -d 2000'

                    output = subprocess.getoutput(command)

                    throughput = re.search(r'Throughput\s\(ops/s\):\s*(.*?)\s', output).group(1)

                    with open("../../result-" + ALG + "-" + nbThreads + "-" + listSize \
                            + "-" + updateRatio + ".txt","w+") as f:
                        f.write(throughput)
                    successNumber += 1
                    print(successNumber)
                except:
                    print(ALG + "-" + nbThreads + "-" + listSize + "-" +updateRatio)
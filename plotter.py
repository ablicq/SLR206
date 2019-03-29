"""
Prepare a graph depicting the throughput as a function of the number of threads
for the four algorithms, for some representative list size and update ratio
"""

import matplotlib.pyplot as plt

ALG = ['CoarseGrainedListBasedSet', 'HandsOverHandsSet', 'OptimisticSet', 'LazyLinkedListSortedSet']
nbThreads=  ['1', '4', '8', '12']
listSize = ['100', '1000', '10000']
updateRatio = ['0', '10', '100']

plotStyles = {
    'CoarseGrainedListBasedSet': 'b+',
    'HandsOverHandsSet': 'rd',
    'OptimisticSet': 'go',
    'LazyLinkedListSortedSet': 'm*',
    '0': 'bs',
    '10': 'rd',
    '100': 'go'
}

def getThroughput(algo, nbThreads, listSize, updateRatio):
    with open("lame_results/result-" + algo + "-" + nbThreads + "-" + listSize \
              + "-" + updateRatio + ".txt") as f:
        return float(f.readline())

lS = listSize[1]
uR = updateRatio[1]

x = nbThreads
for algo in ALG:
    y = []
    for nT in x:
        y.append(getThroughput(algo, nT, lS, uR))
    plt.plot(x, y, plotStyles[algo])

plt.xlabel('Number of threads')
plt.ylabel('Throughput')
plt.legend(ALG)
plt.show()

"""
Prepare a graph depicting the throughput as the function of the number ofthreads,
varying the update ratio, for the list size 1k
"""

for algo in ALG:
    for uR in updateRatio:
        y = []
        for nT in x:
            y.append(getThroughput(algo, nT, lS, uR))
        plt.plot(x, y, plotStyles[uR])

    plt.title(algo + ' algorithm, varying update ratio')
    plt.xlabel('Number of threads')
    plt.ylabel('Throughput')
    plt.legend(updateRatio)
    plt.show()
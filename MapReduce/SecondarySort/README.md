When using only 1 reducer.
1) There is no need for partitioning.
2) Compositekey and FullKeyComparator will be enough sort on multiple fields(state, donor, total)
NOTE: Compositekey sort all the keys in ascending order. But since we needed to sory the 3rd filed of the key in descending order
we used FullKeyComparator.

When using more than 1 reducer.
we 1st create a partitioner(NaturalKeyPartitioner) based on "state" field of composite key.  It partition the data into number of files (equals to number of reducer we want) based on the keys. So that all the same type of keys would go to same file.
job.setPartitionerClass(NaturalKeyPartitioner.class)

Now we group all the partitioned data bases on "state" using NaturalKeyComparator






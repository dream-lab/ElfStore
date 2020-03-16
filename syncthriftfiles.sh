#thrift --gen java EdgeServices.thrift
#thrift --gen py EdgeServices.thrift

thrift --gen java FogServices.thrift
thrift --gen py FogServices.thrift

cp ./gen-java/com/dreamlab/edgefs/thrift/*.java ./src/main/java/com/dreamlab/edgefs/thrift/
echo "Thrift java files are synced"
cp -r ./gen-py/ ./cli/
echo "Thrift python files are synced"

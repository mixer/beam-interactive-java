sourceDir = src/main/java
resourcesDir = src/main/resources

protoc:
	@protoc --java_out=$(sourceDir) $(resourcesDir)/tetris.proto 
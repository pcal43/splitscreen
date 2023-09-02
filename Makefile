

.PHONY: clean
clean:
	rm -rf build common/build fabric/build


.PHONY: jar
jar:
	./gradlew remapJar
	ls -1 fabric/build/libs

test:
	./gradlew test

.PHONY: release
release:
	./etc/release.sh

.PHONY: docgen
docgen:
	./etc/docgen.sh

.PHONY: ide
ide:
	./gradlew cleanIdea idea


.PHONY: deps
deps:
	./gradlew -q dependencies --configuration runtimeClasspath


.PHONY: inst
inst:
	rm -f ~/minecraft/instances/1.20.1-fabric-dev/.minecraft/mods/splitscreen*
	cp fabric/build/libs/splitscreen*-fabric.jar ~/minecraft/instances/1.20.1-fabric-dev/.minecraft/mods/


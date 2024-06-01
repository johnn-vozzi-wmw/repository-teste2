# wmwvendasapp

## build
- Build de beta
```shell
mvn install -Duser.language.format=pt -Dfile.encoding=ISO-8859-1
```
- Build de beta com scripts
```shell
mvn install -Dbuild.script=true -Duser.language.format=pt -Dfile.encoding=ISO-8859-1
```
- Build apenas do wmwvendasapp-binaries.jar para utilizacao no [totaluitest](https://github.com/wmwsystems/totaluitest)
```shell
clean install -Dbuild.tc.app=false -Duser.language.format=pt -Dfile.encoding=ISO-8859-1
```

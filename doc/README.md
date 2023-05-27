### テンプレート
```bash
sbt new akka/akka-quickstart-scala.g8
```

### コード生成
```bash
java -jar openapi-generator-cli.jar \
generate \
-i openapi.yml \
-g scala-akka \
-o ../tacos-backend
```

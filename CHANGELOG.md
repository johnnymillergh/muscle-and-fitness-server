# [0.0.2](https://github.com/johnnymillergh/muscle-and-fitness-server/compare/0.0.1...0.0.2)  (2021-07-16)

### Bug Fixes

* **$auth-center:** read nested JSON object as Java
  object ([af101b2](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/af101b201c43c3d4df7891c46e6d3fd2381443ab))
* **$MyBatis:** correct interceptors
  order ([3647951](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3647951d4019220e181b4281d86e4af2907b961f))
* **$POM:** add test
  dependency ([401587d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/401587df23c74e3b245fb7e9cecce666612db93d))

### Build System

* **$OpenJDK:** update Adopt OpenJDK version to
  x86_64-alpine-jre-11.0.10_9 ([84d737a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/84d737af1c5630845a4ef88b4ef0c51136ab383a))
* **$Redis:** update redis.conf for redis
  6.0.10 ([e522c12](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e522c126f611a88c919ac624f55fa6e5fd82a694))

### Code Refactoring

* **$Docker:** abstract Docker environment variables and common
  constants ([8cc6a36](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/8cc6a36a80f49d090fe669f086fbd99c928ad55f))
* **$muscle-and-fitness-server:** enhance validation - @NotEmpty ->
  @NotBlank ([b4150f1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b4150f1c42d9a2ca36eb9dca36456d5154aef07c))
* **$service-registry:** remove
  module `service-registry` ([61bc7cb](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/61bc7cb0301f7c20f28b11ecde85ada7ab36a56c))
* **$Starter:** rename `spring-boot`
  to `spring-cloud` ([2529e45](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/2529e450061eff00b883d5a0544792f818489f63))

### Features

* **$api-gateway:** set HTTP header "
  X-Username" ([d83fe38](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/d83fe38b968c2d9987466b9df8996778064943ef))
* **$api-gateway:** support request rate
  limit ([c0dc896](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c0dc896603ea484de269555dad869558fedf8e3c))
* **$APIGateway:** add global exception
  handler ([7bb6ffa](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7bb6ffad68fad643ec1f506f49859e2fdecb7601))
* **$AuthCenter:** access other service by
  RestTemplate ([f8df08c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f8df08c53fec6380fc1457ce01c2b225adfc6eb0))
* **$AuthCenter:** add API for getting service
  info ([c0b00f3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c0b00f3b26fc645d86f085fce5e23becca2e3bd7))
* **$AuthCenter:** add logout
  API ([66c6717](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/66c67179c7493343f1f4bfb807c15385c2abb684))
* **$AuthCenter:** enable Redis
  cache ([6c14c7b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6c14c7b83512317b79a9245c7e24bb6feb34c400))
* **$AuthCenter:** expose login
  API ([fe3f211](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/fe3f211454df32d907950ab27d7701655bf41683))
* **$AuthCenter:** get services
  info ([97aadce](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/97aadcef7e3febbdd1a05403d7f72dc2c23d4c43))
* **$AuthCenter:** support admin
  authorization ([6e87a2e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6e87a2e9c2ba2debb9e46254e29dbf764553a6a0))
* **$Authorization:** enhance authorization flow - check HTTP
  method ([01a50de](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/01a50de49b2354a2d1bd29864e3235ec68a1eaeb))
* **$Consul:** enable Consul config
  center ([9887360](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9887360ce74d3028b2f367b7ea8a0f773add08b3))
* **$Consul:** support Consul; abandon Netflix
  Eureka ([abc1cf8](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/abc1cf835cdb46d7a23e40f14557c21b5265184d))
* **$ELK:** integrate ELK
  stack ([a42e87a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a42e87a7691011b391c09f9fb20192a100c3bf22))
* **$Excel:** add abstract excel import
  class ([901edbd](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/901edbd6f63b116f53dd942769aba70ef88f93f9))
* **$gateway:** complete authentication
  flow ([b0a91e0](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b0a91e048743b76abbe66ce484908c4ea3c212d7))
* **$gateway:** integrate Spring
  Security ([f092c6b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f092c6b3b55baddc2cebfee556c11c001083a145))
* **$gateway:** make Swagger and Spring Security work
  together ([c2c4721](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c2c47212e5774cb374b6cf20f127f80f482abcd6))
* **$Gateway:** add reactive openfeign
  support ([f0b7567](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f0b756772e1e63f001ae17230c36e076f81c6e1d))
* **$Gateway:** add reactive Redis
  support ([2307c71](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/2307c711a94951b9731f78b3d27b9930f59a2cd4))
* **$Gateway:** implement non-block
  authorization ([b94944d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b94944d562f31baa3a6eb3549c0b40d8d17d76f7))
* **$Gateway:** support ignored URL for WebFlux
  security ([efb83ad](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/efb83adafa2a2e8491d72577194b73893d5e68ca))
* **$Jackson:** add DateTime
  serializer ([69548c4](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/69548c439fb56c1d7dd5d9f2e552a1cc0392929c))
* **$Java:** handle LocalDateTime, LocalDate and
  LocalTime ([778e152](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/778e15220e34889e9cad41fe783ece8a601ba86a))
* **$Minio:** upload resource to
  Minio ([3dc2dc0](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3dc2dc0a1e095bca72a224ad9c62749e40d51656))
* **$MinIO:** integrate
  MinIO ([4db17c0](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/4db17c0647a7d24aacb49f6b0db3662678ee3d9a))
* **$Pagination:** abstract page response
  bean ([28928b8](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/28928b8bee5330081590dc3b3c1d63a1fb0fbf07))
* **$Quartz:** add greeting Quartz
  job ([7993f40](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7993f40659a5f31e7aecdfbbbf2b0453f41406ba))
* **$Quartz:** integrate
  spring-boot-starter-quartz ([5a6d71d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/5a6d71d99d43461ed68dc1591d481267ba64ea29))
* **$RabbitMQ:** add RabbitMQ Docker
  container ([414880d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/414880daa175f7ccf06cf17336935becc49f6f08))
* **$Starter:** abstract startup
  analysis ([0802aa2](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0802aa2008aa47cb2c1b5424e64ff48873b6da7a))
* **$Starter:** add
  HttpApiScanHelper.java ([629e066](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/629e06671be275bb7c5e47739fb8efa58d67a35f))
* **$Starter:** integrate
  RabbitMQ ([9022f77](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9022f777e53e0df8ce3a38aeca1c72489950d0f7))
* **$Starter:** intercept SQL
  exceptions ([9f5a16f](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9f5a16f66b96706c1b1a8334ed66342fcf121ddc))
* **$Starter:** provide exposing HTTP
  resources ([4f2ebdc](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/4f2ebdcd31c0df8f17ac3c711baf8164ee4b71a6))
* **$Starter:** support generating recursive
  tree ([624e5ab](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/624e5ab887935beeccb0fb6a6329e5fbc3349dc8))
* **$static-resource-center:** get resource from
  Minio ([da01f3c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/da01f3cfc07d03304dd7c2320ee17a91970f5f49))
* **$static-resource-center:** support partial
  response ([7ddf6a9](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7ddf6a975c6976627543f2a3767b736413d54d67))
* **$STOMP:** support STOMP over
  WebSocket ([be76707](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/be767070f3f484a19ca115578cb00f0b4fc30cbf))
* **$Validation:** support Date and LocalDateTime range
  validation ([f9898be](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f9898bec3917beec91b61b826b5fd02f2e388d7a))
* **$Validation:** support date time range value
  validation ([96c8b83](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/96c8b83dff2769ecb43c7bcb6f5211f59e34ead4))
* **$Validation:** support enum value
  validation ([0951f97](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0951f974289e639e0da28c055a34a3afd49b597b))

### Performance Improvements

* **$api-gateway:** refine exception
  handling ([b5e1340](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b5e134022401751f420d10b1ada254ddda686777))
* **$api-gateway:** refine slf4j
  Logger ([af590f5](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/af590f5b2b805b8560abfd2dddd4b0e4de72195e))
* **$ApiGateway:** capture
  FeignException ([c9cdee4](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c9cdee4d24a635764cb21ddc8c44dcfde18cd39e))
* **$auth-center:** cache role info by user
  id ([e9d935b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e9d935b183c05287bbc1c1a78f13a1e6140bbc02))
* **$auth-center:** cache user info by login
  token ([2bc9115](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/2bc9115ed0d055a00e87e1820f2bced0caf9b99f))
* **$auth-center:** reduce injected
  dependencies ([a12f130](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a12f130c39af3d8f04b7af54d5b352bf92258676))
* **$DataSource:** reduce instances of data
  source ([581ac97](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/581ac97cb2f4fcd1c6f0e87dd6c56c309c9967d8))
* **$Docker:** update container
  dependencies ([aca40f6](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/aca40f68da38392e7442aca559b957c3a6225a1e))
* **$ELK:** disable
  ELK ([ecdb3e2](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ecdb3e20d59f77a260bcfa0e3f9547e49125728d))
* **$Excel:** make `maximumRowCount`
  configurable ([8865ead](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/8865ead8266c0f356d71ad7fa7cba67e000b19d2))
* **$Java:** migrate java.util.Date to
  java.time.LocalDateTime ([a354070](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a3540706839b6f1d84ed6fed29d569b3626e970f))
* **$Knife4j:** roll back to
  2.0.8 ([3c0ce61](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3c0ce61247e189de4ef6a21307098cb79a74ccf6))
* **$maf-mis:** merge micro services as '
  maf-mis' ([7d7284c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7d7284cd2498ce80fe26d4a8b31f35c807944701))
* **$MySQL:** Docker supports MySQL cluster based on binary
  log ([bab526a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bab526ab552fb9018b3dd3bd9c2f2dce6f7b5e1a))
* **$MySQL:** MySQL connection
  retry ([b076feb](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b076febb0291e9061dd27cb4eb2c5337c66c1e91))
* **$MySQL:** reduce unnecessary replicate
  db ([3976af1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3976af1bd4dc43b45d0f102f9113d4043298a0d5))
* **$MySQL:** simplify MySQL configuration for
  docker-compose ([7f4a017](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7f4a017a41ddb7caa8df85884923e58524e8ad0e))
* **$MySQL:** support auto start
  replication ([a52619c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a52619c97c974963731f03a47ef9fabedf37a556))
* **$POM:** reduce useless
  dependencies ([447e072](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/447e072a8132a715672f724a202ec3f71bb11aae))
* **$POM:** update
  dependencies ([9ef9708](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9ef97081fd464796993c813d2fec27f4d9cedc6b))
* **$POM:** update Spring Boot Admin version to
  2.4.3 ([ad04895](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ad048950d48aca490cb3af0c56f0794cffd60353))
* **$POM:** update Spring Cloud version to
  2020.0.2 ([bc2241f](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bc2241fdeba36fa47594ab041113e964016ad354))
* **$Spring:** refine CORS
  configuration ([cbb9c7c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/cbb9c7c56c85f7e1c10a7e6c282502ab65b4c3fa))
* **$spring-boot-admin:** exclude unnecessary
  dependencies ([305e12b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/305e12b5a7fcd6a59ec1e0cc79c594c4c1aa8d1e))
* **$SpringCloud:** update version to
  2020.0.3 ([010e2c3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/010e2c32ddda0130ff5da9b9ed1f2569ab65d50d))
* **$starter:** define the order of controller
  advice ([34706c9](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/34706c924d37eb6d79bbe661fb792f0eafb5278e))
* **$starter:** improve Access Specifier Manipulation
  issue ([435ea3b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/435ea3bf70d4ef6b0ce6d47de2106ef7326bd62f))
* **$Starter:** dynamic MinIO
  client ([7d030a3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7d030a33af5310f247a403850b790efe59a2e8ec))
* **$Starter:** dynamic SFTP
  client ([4c37cc4](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/4c37cc4120c74e294f592ff9b80495eb257b5b92))
* **$Starter:** enable Okhttp for
  OpenFeign ([0aec942](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0aec942dcb4472b77a5028b0a9e3dc04f131f989))
* **$Starter:** support switching dynamic data source on runtime based on
  SQL ([7dbcfc9](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7dbcfc956aeef73a4818658de8ac4e2518969888))
* **$Swagger:** correct Swagger
  switch ([7804da0](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7804da0257fc8f8e34fa4518f82896e2fc210aaf))
* **$WebSocket:** use RabbitMQ STOMP as message
  broker ([c666ba7](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c666ba72c2ffa775900f7b5218cdf96c6218cc9f))
* update
  dependencies ([6f55581](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6f555811df4778a29fa76ae38be7294da2f1959c))
* **$api-gateway:** add IP key resolver for request rate
  limiter ([ac7aabc](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ac7aabcc3a83e471377ab1ccf269ddf808b581c8))
* **$api-gateway:** pass rate limiter configuration
  dynamically ([0bd97b0](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0bd97b0262835ee7bb8b1240cafeec03d4010a1d))
* **$api-gateway:** set Swagger ignored URL on
  Consul ([699dd0a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/699dd0a3da15b9a097de3c3b3829b034848431d2))
* **$ApiGateway:** enhance global exception
  handler ([0d3f265](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0d3f265c58f9b7ed51d3c0ab324338925727e0bc))
* **$ApiGateway:** handle service not available
  exception ([235bcb8](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/235bcb8c9c51ba4f28d002512a58b5b1722f4e63))
* **$ApiGateway:** remove JJWT
  dependencies ([50c0597](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/50c05973c42b7daba898bbfd00347bd081a64673))
* **$ApiGateway:** rename module `Gateway`
  to `API Gateway` ([c367ca3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c367ca3f8920c7586d219744eecc6dd052cb5a77))
* **$ApiGateway:** shorten module
  name ([9d7e759](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9d7e759d514f627264616063745863476ded2f25))
* **$ApiGateway:** use lombok val
  annotation ([aec0662](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/aec0662ddd1297aee880fad12d5bb9f3690f2471))
* **$APIGateway:** handle
  WebClientResponseException ([8c4cd5a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/8c4cd5af945391d049a79b4d2605b884dc5cfd8a))
* **$APIGateway:** refine remote API
  calling ([4f5a20d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/4f5a20d8d8b4f5b5de463ce2d7fe42c2f0cb3752))
* **$APIGateway:** simplify global exception
  error ([ff82751](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ff8275119895628dce5eceb7644f5facb0747edf))
* **$auth-center:** add PermissionTypeList for remote
  API ([6928368](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6928368c89a9af174bd55331b70faa8a94259656))
* **$auth-center:** configure ignored
  service ([78c069b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/78c069b0651e88412cdabf3bad782bf7b31739a6))
* **$auth-center:** refine API
  parameter ([b5b5346](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b5b5346e535d76690ffae9a3d43ec4397a3c8611))
* **$AuthCenter:** customize
  RedisTemplate ([22c9cfb](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/22c9cfbfdd056b11832e5511acb354214f152d07))
* **$AuthCenter:** refine null check for getting service
  info ([fc363fe](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/fc363fecfb875ac9ffde21cc4bbbb0819d4cafd2))
* **$CMD:** check Java major version before running
  service ([0d06475](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0d06475081e6df1a8a6e8f3ffa24a051d21901c4))
* **$Common:** remove useless
  dependencies ([1df12c5](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/1df12c53b5c138879e94852cbd993f07ac760a30))
* **$Consul:** deregister service when
  unavailable ([d36aac3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/d36aac3c59e0a40da2c2901d00dd4e5a59108e8b))
* **$Database:** abstract MyBatis Plus configuration; delete Druid
  configuration ([e88b4d6](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e88b4d6b669ee4a63ab3d562c78942b6c23538ad))
* **$Database:** update data source
  configuration ([d343369](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/d3433699c8fa254d0e976467a57485cd05fdefdf))
* **$Docker:** restrict container
  dependencies ([67c968d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/67c968d603cbaa7af32a3d27eccdd8d8845a5820))
* **$Docker:** simplify docker compose by using environment
  variables ([3216a95](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3216a956b40791b8980378a4716129324e0a0f75))
* **$Docker:** specify Docker container
  tags ([e416c2c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e416c2c442be55212d956d554c00332edc8feabb))
* **$Docker:** support Docker health
  check ([7a2ce2b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7a2ce2b0df74c82b182441a7cfb5cec46d4b9366))
* **$Docker:** update volume mapping for
  openzipkin/zipkin-cassandra ([abb784f](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/abb784f2c2e652aaf5709820a8779171323fd0bb))
* **$Druid:** migrate to druid-spring-boot-starter
  1.2.4 ([0a7ca5e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0a7ca5e174ec01c70d6e7d4c3fc021b1a189caae))
* **$ELK:** enable elasticsearch
  security ([bbe59a9](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bbe59a90fab66a9ea2a45ceb2271d721dfaa23a0))
* **$Excel:** make variable declare as var and
  val ([e07452b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e07452b9eaa541062f59da7ba5285bb53cc8ea35))
* **$Excel:** translate into
  English ([60f8cd1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/60f8cd167c20b7157df0b567282e81dfe40c90ae))
* **$Exception:** refine global exception
  handling ([dae962b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/dae962b979384a10770892770753b825e6165b52))
* **$Gateway:** capture
  SecurityException ([77d6d34](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/77d6d34b60a73e523619b740d0c07f5291e5accb))
* **$Gateway:** dynamic web security
  switch ([b5f4333](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b5f43336185fb2a6b55200eb8f6507e31dac878c))
* **$Gateway:** reduce unnecessary
  log ([7ba75c3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7ba75c30c1acfae01603cd3aac4bebb41adc392f))
* **$Gateway:** refine access
  log ([281a958](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/281a958d4a86731db52195bb5078fa072d09cd74))
* **$Gateway:** refine RBAC
  process ([fed3ea7](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/fed3ea7a4de6677f5aef1764f82fff3936208dac))
* **$IO:** use buffered IO stream to reduce IO
  times ([f6c32ee](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f6c32ee5f3f6f952392113168948adc4325d2152))
* **$JVM:** increase JVM heap memory; dump heap on OOM
  error ([45c528b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/45c528bce812d9dba70c0bcf0160c32ac6aa0ba9))
* **$Log:** enhance web request log
  format ([a359d07](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a359d07c764bf1c83538e8834d79d7dc1193fef2))
* **$LOGBack:** log file contains
  HOSTNAME ([60b2b73](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/60b2b731b26ac959ade937a6fe3f9237bc4af0f9))
* **$LOGBack:** set container
  hostname ([ac3c130](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ac3c1304455b66a8f233914cadf1a110b5161b13))
* **$LOGBack:** set log file by
  ${spring.application.name} ([ec01c8c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ec01c8cedf61c01126d9466c9bcb0e899e2bdfdb))
* **$muscle-and-fitness-server:** define basePackage by groupId; devtools
  switch ([e8a86b1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e8a86b182c85278faf989757757478ccab5278b8))
* **$muscle-and-fitness-server:** update
  dependencies ([e67a840](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e67a840a0fef09f01e5903f7018872930f62514b))
* **$MyBatis:** field auto
  fill ([6497bcc](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6497bcc4ca2ea913124494e903ac4cf80bd6a5b4))
* **$MyBatisPlus:** enable logic
  delete ([e48fafa](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e48fafa45e6ed9111bca6497d493abbd749f2503))
* **$MyBatisPlus:** update
  interceptors ([9e3ce5c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9e3ce5cc33ac985b04eb7eaf7175de5bf1078d48))
* **$OpenJDK:** migrate to
  adoptopenjdk/openjdk11 ([9b65893](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9b65893a22e845a7775db7f80ec988e6436b6038))
* **$ORM:** migrate ORM library to
  spring-boot-starter ([0e66476](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0e6647615317690a16193f139fa174dbd459b527))
* **$ORM:** migrate Redis library to
  spring-boot-starter ([7af0ccc](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7af0ccc612067f2608156cc1bd376fcb8cea8ed8))
* **$ORM:** migrate sftp-integration to
  spring-boot-starter ([c860ffa](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c860ffa9ab08ee2aede974484b4c5517ce9c8e0d))
* **$POI:** close workbook when destroying locale
  context ([ebebd3e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ebebd3eaa482f19c43fef45f9458d88ee16bd9a4))
* **$POI:** update Apache POI version to
  5.0.0 ([7b60af5](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7b60af5ba65113c32c48520e94ac2758326032b2))
* **$POI:** update Druid version to
  1.2.5 ([1c1a033](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/1c1a03311d8dd6d9eb796f929ecf2493a239763a))
* **$POM:** flatten parent
  dependencies ([e69537a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e69537a83c9f610d39e0d2e0d124dc7e0a115465))
* **$POM:** refine dependency
  structure ([d82c393](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/d82c393f4161e3b1266d1b586a572fdee2c54a4b))
* **$POM:** update cn.hutool:hutool-all:
  5.5.7 ([e7f2193](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e7f2193e76cc15b986692114dddbad935c3f7e1b))
* **$POM:** update Hutool version to
  5.5.8 ([7288bc2](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7288bc2ce02ba658db96bcdb7104f4ec2df7cef0))
* **$POM:** update hutool-all to
  5.5.4 ([e38a37c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e38a37c69b48a4495f1855753571dfe5c77ad0ff))
* **$POM:** update mybatis-plus version to
  3.4.2 ([1eeefcc](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/1eeefcc9b77a3be09528b4caf34c4ce77a3ac110))
  ,
  closes [/github.com/baomidou/mybatis-plus/blob/3.0/CHANGELOG.md#v342-20210115](https://github.com//github.com/baomidou/mybatis-plus/blob/3.0/CHANGELOG.md/issues/v342-20210115)
* **$POM:** update Spring Boot version to
  2.3.8.RELEASE ([bc21a41](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bc21a41db86e2d4af0d79616c4bc2fc1e13f59c2))
* **$POM:** update Spring Boot version to
  2.4.3 ([b955e61](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b955e61fbd7fdc6e47946ca1c1de3498846efbec))
* **$POM:** update Spring Cloud to
  Hoxton.SR9 ([9ab311b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9ab311ba65b3babe18e464e39e65c0269d55d124))
* **$RabbitMQ:** add RabbitMQ health check; set container
  dependencies ([660bf49](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/660bf49ba8a1d0de4f1ada270fb763e28e99890f))
  ,
  closes [/devops.stackexchange.com/questions/12092/docker-compose-healthcheck-for-rabbitmq/12200#12200](https://github.com//devops.stackexchange.com/questions/12092/docker-compose-healthcheck-for-rabbitmq/12200/issues/12200)
* **$RabbitMQ:** make JSON message converter as
  default ([1b9bb6d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/1b9bb6ded2005837fe8727eababb36be6ab5175f))
* **$ReactiveStarter:** abstract access log
  filter ([d29ba21](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/d29ba212a57e21b6c9f56731e3860b3915296015))
* **$ReactiveStarter:** abstract
  beans ([8d1f040](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/8d1f040b937fd7b16ad415815d23b8cb74ad6127))
* **$ReactiveStarter:** abstract project
  property ([aafac56](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/aafac56c820fa406f77d6ae0ba6b3c7aba517e41))
* **$ReactiveStarter:** refine
  dependencies ([f8905bb](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f8905bb74c438a12f8a9fa3aba46d95239bec64a))
* **$ReactiveStarter:** set HTTP header as
  application/json ([886124a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/886124ad66e8f9b3d0f8a6b704c58c0707c5ca29))
* **$ReactiveStarter:** set the order of access log filter as
  -500 ([1cb0600](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/1cb06000a936caa2f1a2b310dc085c3a31d3ff47))
* **$Security:** enhance authentication
  process ([c897611](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c8976113a101ada7712433e580837226bdb8d49c))
* **$Sleuth:** add
  dependency `spring-cloud-starter-sleuth` ([8aba675](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/8aba675ab8c85733256f7561533813043a2b1cae))
* **$SpringBoot:** enable shutdown
  gracefully ([6360027](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/63600273bd3b8947bbbdfeeddd07f7278995c800))
* **$SpringBootAdmin:** don't check Consul service
  health ([e5c8a3d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e5c8a3df800286d71e8716651cce8256818e7600))
* **$SpringCloud:** update Spring Cloud version to
  2020.0.0 ([18f5843](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/18f5843c5af05a37c8c2481f4e8a3d138ee57644))
* **$SpringCloud:** update Spring Cloud version to
  2020.0.1 ([36ecc33](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/36ecc338781a516e20840726dd0d07fc1f7d6988))
* **$Starter:** abstract common controller and
  service ([3ab8943](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3ab8943e1e2a126cc899f680b7cf18bfaa285820))
* **$Starter:** abstract
  GlobalErrorController.java ([e22ca36](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e22ca3617c3077de3b0cd3fbf91c86e146acf589))
* **$Starter:** abstract project
  property ([b6996c8](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b6996c86036de985658b4e4675d516fa539382a5))
* **$Starter:** abstract Redis
  configuration ([9687151](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9687151989353c4ec4a453f810bde131c278bbba))
* **$Starter:** abstract rest template
  configuration ([07dfbff](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/07dfbffc91137d52a1d996742372244b90ac0499))
* **$Starter:** abstract SFTP and web security
  configuration ([3306ec4](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3306ec4defb038e20d6a3294f07e61f4d015be7c))
* **$Starter:** abstract Swagger 2
  configuration ([5ae8a56](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/5ae8a564a71c4f2cfbc89ede097ed92f79ca1987))
* **$Starter:** abstract swagger
  configuration ([2d27aef](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/2d27aeff84a05ee88e172c92a81fd34776149378))
* **$Starter:** dynamically init
  bean `WebRequestLogAspect` ([e3176ad](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e3176aded0ef69aaad569218747591e7cd6bdb2b))
* **$Starter:** enable Spring Boot async
  task ([418fc43](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/418fc433857bbc80a7be9af7e48bb388ed468c00))
* **$Starter:** enhance message
  format ([be3cc41](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/be3cc41a8833654247b510707f55722286ae7dc7))
* **$Starter:** migrate static web resources to
  spring-boot-starter ([83ff9eb](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/83ff9ebca68273ffc19751fc7bee20768dbc4047))
* **$Starter:** reduce unnecessary access
  log ([7e8a816](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7e8a816575c3348d6ecdd91612521b7abb270785))
* **$Starter:** refine
  dependencies ([bd1e5b3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bd1e5b38fbf4180e567add86b7b5d2890464608e))
* **$Starter:** refine error
  message ([753946c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/753946cbd6680ac035edaadcaf0cb81b4af88b4f))
* **$Starter:** simplify internationalization (
  i18n) ([b42bc7b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b42bc7b3812db702dd0d5315ac09e909cfa5bb4a))
* **$Starter:** support internationalization (
  i18n) ([1c8b38b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/1c8b38bff6765f8206c1e3a3bf4725f9f9bec01b))
* **$Starter:** use lettuce for
  Redis ([c40c304](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c40c3042f3bd0e0788159c9cdde681c81b9ef562))
* **$Swagger:** correct Swagger switch
  logic ([e72e8de](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e72e8def0ac935a5ec5cd85cfb75958fbbe2fb83))
* **$Swagger:** support service
  filter ([f07e0de](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f07e0de75020d2a5437932aa62257bcdaf9d2cf2))
* **$universal-ui:** abstract web pages to sub
  module `universal-ui` ([68a34d3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/68a34d343da05644ce8b1d6a569a67bf107b98db))

### Reverts

* **$AuthCenter:** remove
  TODO ([de11944](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/de119446bd6c6590ab945e1bbcaa89d4e854f548))
* **$ELK:** enable
  ELK ([58ca161](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/58ca16111230224c8d9ff73bab2ca594dbdd3fcf))

### BREAKING CHANGES

* **$starter:** improve Access Specifier Manipulation issue
* **$IO:** improve IO efficiency
* **$OpenJDK:** update Docker image from adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.10_9
* **$Docker:** abstract Docker environment variables and common constants

[skip ci]

* **$ELK:** aggregate logs from all applications
* **$api-gateway:** refactor MafConfiguration.java; update configuration key
* **$CMD:** check Java major version before running service

[skip ci]

* **$muscle-and-fitness-server:** enhance validation - @NotEmpty -> @NotBlank

[skip ci]

* **$Starter:** use lettuce for Redis
* **$api-gateway:** support request rate limit
* **$Starter:** rename `spring-boot` to `spring-cloud`

[skip ci]

* **$Docker:** simplify docker compose by using environment variables
* **$Consul:** enable Consul config center
* **$service-registry:** remove module `service-registry`

[skip ci]

* **$Consul:** support Consul; abandon Netflix Eureka
* **$SpringCloud:** update Spring framework dependencies: Spring Cloud 2020.0.0, Spring Boot 2.4.2

[skip ci]

* **$Starter:** reduce package size by migrating static web resources

[skip ci]

* **$LOGBack:** set log file by ${spring.application.name}

[skip ci]

* **$LOGBack:** log file contains HOSTNAME

[skip ci]

* **$Redis:** update redis.conf for redis 6.0.10

[skip ci]

* **$Authorization:** enhanced authorization flow - check HTTP method
* **$Starter:** rename MAF Spring Boot Starter package name; set bean injection order
* **$ApiGateway:** abandon custom HttpStatus, use unified Spring Web HttpStatus
* **$ApiGateway:** remove module `API Portal`; rename module `Gateway` to
  `API Gateway`
* **$ReactiveStarter:** abstract access log filter
* **$Starter:** reduce unnecessary access log
* **$Gateway:** implement non-block authorization
* **$muscle-and-fitness-server:** define basePackage by groupId; devtools switch

# 0.0.1 (2020-12-13)

### Build System

* **$Configuration:** update each environment
  configuration ([72b6c97](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/72b6c9765dbad7109a05c71689f566899acd9cc6))
* **$Docker:** build Docker image when it's Maven
  install ([93ec60d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/93ec60dfb85052d9f76356e9fff6ec926057e4be))
* **$Docker:** update zipkin
  container ([df5de9d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/df5de9d5f749239b1e0688d9dac9891bb902be2a))
* **$Docker:** use 20-bit block private network
  172.16.1.0/24 ([44a7450](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/44a7450f5df5fb31110b832995779411bf965b87))
* **$DockerHub:** change DockerHub repository
  name ([b6067aa](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b6067aa3967e047ab013b8bec4fc278e222fe43f))
* **$Eureka:** simplify Eureka
  configuration ([b74c761](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b74c7617c731d0e2bd91bd789a69d13b4ac5cf59))
* **$Shell:**
  add `auto-run-mac.sh` ([5e401d7](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/5e401d790b04b2db740a2513496eb2518dd89c16))

### Code Refactoring

* **$muscle-and-fitness-server:** remove demo module ([cd6a6d4](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/cd6a6d4d69c8b17708cb1c45811385dd8b458c33))
* **$Project:** change package name, groupId, DockerHub image repository ([4f65ad7](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/4f65ad795d9fdc48f49cfce8d4ab116a85786cb7))

### Features

* **$CMD:** add auto-run-windows.cmd ([7e03aa7](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7e03aa73a4ec3d6220c4256b7b07ccd3b1ea7f63))
* reduce env effects on build, logs ([982ff20](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/982ff20e085c6a2870f492960d67e11f463a0fea))
* **$api-portal:** use remote service; enhance web security exception handling ([d80b182](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/d80b182bc0e656b4cc292e7fda4754fa1feb869e))
* **$API-Portal:** add register and login ([e4e0318](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e4e031819b73eb1683723a02378d630cf4636f84))
* **$APIPortal:** add new module - API Portal ([c5ac76e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/c5ac76ebc4b738ead971d0a1a1c536699dd84680))
* **$auth-center:** migrate codes from `api-portal` ([9b07500](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9b075005710961a02c654a28390206d92092b269))
* **$auth-center:** provide remote service for `api-portal`: get permission list by role id list ([8aca3d0](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/8aca3d0ab407b834328c3f1ed98bb2b20326f9e7))
* **$auth-center:** provide remote service for `api-portal`: get permission list by user id ([7d46e53](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/7d46e53fc0e2694be928f1bff98e50cf0376f3d0))
* **$auth-center:** provide remote service for `api-portal`: get role list ([a472f46](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a472f46a8dcc674c90b4f3d01731be48dec48da9))
* **$auth-center:** provide remote service for `api-portal`: save user for registering ([f7a0d7e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f7a0d7e341b8300ca8061bcb910942e28e9cd886))
* **$auth-center:** provide remote service: get user for `api-portal` ([71daf55](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/71daf5539abf5b301beb151bcc753fd9138dece4))
* **$Common:** add new module `Auth Center` ([720475b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/720475b4e991127c1cff41d49adab37339c24ec1))
* **$Exception:** capture BadCredentialsException ([9e31637](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9e31637ecf85623f3d4edcdaa30638081b9ad982))
* **$Exception:** catch IllegalArgumentException ([3c5eb03](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/3c5eb03fb326551ea4d5c0cc7583a14d267e31e6))
* **$Gateway:** secure gateway swagger ([9207062](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/92070620a5fe9d5e5caa67eaf6c7efdfa0b0d0e3))
* **$HTTPStatus:** add more HTTP statuses ([4dc81c3](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/4dc81c32851eee068c19cb31e5620bf950a7186a))
* **$MySQL, docker-compose:** init MySQL database automatically ([68e0ddf](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/68e0ddfec93aa238fb4d435331e68a3797a6eaa3))
* **$RBAC:** migrate RBAC codes from project `jm-spring-boot-template` ([bd559d2](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bd559d2281452b31d944fecc1eaaea03748b16a1))
* **$Redis:** enable Redis log output ([741261e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/741261ed79b8db6642056fd4ee8235072f342157))
* **$Validation:** throw all validation error ([19546e1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/19546e126e111e77fa3deb69fc6c8b8db301398f))
* **$WebLog:** support web log for all HTTP methods ([9de3114](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9de31148bc95375a5f29cd76f3022e60df8fc4f2))

### Performance Improvements

* **$API-Portal:** use var and val for local variables ([ff54ca5](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ff54ca5cdd1e6da0f98fc81845ae4ecfe62b205a))
* **$Authentication:** capture request filter exception ([85d105c](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/85d105c9d661675e17147cef6a20e10ea2159829))
* **$Exception:** simplify trace and message of response ([eae6793](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/eae67930048ca11980518b1e84a83b3d5916d08e))
* **$Exception:** synchronize codes ([645b91e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/645b91e155ec594f08e2fd28d09d77f7f7229f9f))
* **$Feign:** enable feign compression ([a9e69e8](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a9e69e8d8077dcd3132983b9fe3d557ea0648ae8))
* **$Filter:** delete function-duplicated filter `RequestFilter.java` ([48434f1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/48434f15d7b8ba19523d2bb4c8b1089bf8026783))
* **$POM:** delete unused dependency ([6706a97](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6706a97a12475e476abab1a984e4ffc950024809))
* **$POM:** don't use deprecated API ([dfeb345](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/dfeb345f73c1fcb65bab62782d9e3554f9eef4c7))
* **$POM:** shorten the time for Maven lifecycle `compile` ([bac2c70](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bac2c70734843590ec8f52be628bc25a3a855efd))
* **$POM:** update Spring Cloud Hoxton.SR4 ([15e292e](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/15e292eb3312e1f3ec0879ce07e61982b69fd67a))
* **$POM:** upgrade hutool-all to 5.3.4 ([925c8c1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/925c8c1fc20fa844f1afce82e3b7f572290eca1e))
* **$POM:** upgrade JJWT@0.11.1 ([a79dffb](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/a79dffb82fe5654bbbd54b5f0e0c4f237aeaa46a))
* **$POM:** upgrade Spring Boot 2.2.6.RELEASE ([ca483d2](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/ca483d2f45c8c5916ca62e45d037cdd4e0c4d9c7))
* **$POM:** upgrade Spring Boot to 2.3.1.RELEASE ([9342aa9](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/9342aa9e2c837ce2feec83cded704729e2e2560f))
* **$POM:** upgrade to Spring Boot 2.2.7.RELEASE ([76edf55](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/76edf554dfac48d2dcebb0edc12e92a5be5b663a))
* **$Project:** abstract common configuration into custom starter ([6a0a379](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6a0a3799bbee427ad9338cb30e34ae10de47173a))
* **$ResponseBodyBean:** add @NonNull annotation for arguments ([e85bacf](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e85bacf854b3b1506d2245bfb07ed66d9cf82d5e))
* **$ResponseBodyBean:** annotated with @Value ([0c3c94b](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0c3c94ba1c0bc0fc2826346c13d5a2c032f18d95))
* **$ResponseBodyBean:** delete function-duplicated method ([bf47226](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bf47226e64063da98f7155067b9bd8d3be6f3976))
* **$Shell:** refine auto-run-mac.sh ([0b861c4](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/0b861c44c175c73a8823412957b23b12e771e5d5))
* **$SpringBoot:** upgrade to Spring Boot 2.3.0.RELEASE ([f17c044](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f17c04474fe54c23fee574f1da2299f681efc933))
* **$Swagger:** upgrade Knife4j to 2.0.2 ([2f1f63a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/2f1f63aaf70206e34619de6b1060b1baebf6f1d4))
* **$Swagger:** use new enhaced generation of Swagger UI; update M&F favicon ([4b33cde](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/4b33cdeed1826a848b987575738814d9a4dd2b88))
* **$VarAndVal:** synchronize codes ([e961c32](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e961c32df4ce77da65c8548a42ada52e5ab17d1e))
* **$VarAndVal:** synchronize codes ([e94b1d1](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/e94b1d1ece3e5a887af730dfc1f057a46c3bafda))
* **$VarAndVal:** synchronize codes ([941a87a](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/941a87a7b558b40291346a35efe6aae889db9e08))
* **$VarAndVal:** synchronize codes - DruidConfiguration.java, MyBatisPlusConfiguration.java, ProjectProperty.java ([6a235aa](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/6a235aaa5a0ffb1f4ca76b253e4d737914703b90))
* **$VarAndVal:** synchronize codes - ExceptionControllerAdvice.java, MethodArgumentValidationAspect.java ([bc5b365](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/bc5b36592ad4a8a5d1abdb4a1fb1c07b26660b0e))
* **$VarAndVal:** synchronize codes - WebRequestLogAspect.java ([f38dded](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/f38ddedea3a7998141ce7e4e1008c9c60d500481))
* **$WebLog:** trim printed JSON string ([48e7984](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/48e79844627f9b06d9316504176ef4c58860969a))

### BREAKING CHANGES

* **$Project:** abstract common configuration into custom starter
* **$Docker:** support universal volumn mapping, under home directory
(Mac, Linux, Windows)
* **$Shell:** support smart and simple auto run
* **$Docker:** build Docker image when it's Maven install
* **$DockerHub:** change DockerHub repository name
* **$Project:** changed package name, groupId, DockerHub image repository
* **$Configuration:** application-*.yml is corresponding with network configuration with docker-compose
* **$Eureka:** redefine 'instance-id'
* **$Docker:** update network configuration in Docker compose
* **$auth-center:** split RBAC logics into 2 microservices `api-portal` and
`auth-center`
* **$POM:** change Jib execution phase
* **$Common:** support Redis operations
* **$muscle-and-fitness-server:** remove demo module
* **$WebLog:** support web log for all HTTP methods
* **$Swagger:** use new enhaced generation of Swagger UI; update M&F favicon




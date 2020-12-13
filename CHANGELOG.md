# 0.0.1 (2020-12-13)


### Build System

* **$Configuration:** update each environment configuration ([72b6c97](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/72b6c9765dbad7109a05c71689f566899acd9cc6))
* **$Docker:** build Docker image when it's Maven install ([93ec60d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/93ec60dfb85052d9f76356e9fff6ec926057e4be))
* **$Docker:** update zipkin container ([df5de9d](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/df5de9d5f749239b1e0688d9dac9891bb902be2a))
* **$Docker:** use 20-bit block private network 172.16.1.0/24 ([44a7450](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/44a7450f5df5fb31110b832995779411bf965b87))
* **$DockerHub:** change DockerHub repository name ([b6067aa](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b6067aa3967e047ab013b8bec4fc278e222fe43f))
* **$Eureka:** simplify Eureka configuration ([b74c761](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/b74c7617c731d0e2bd91bd789a69d13b4ac5cf59))
* **$Shell:** add `auto-run-mac.sh` ([5e401d7](https://github.com/johnnymillergh/muscle-and-fitness-server/commit/5e401d790b04b2db740a2513496eb2518dd89c16))


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




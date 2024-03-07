# 선착순 쿠폰발급 서비스 구현

## 순식간에 많은 트래픽상황에서 정해진 쿠폰의 갯수 만큼만 발급되는 서비스 구현

1. MariaDB PESSIMISTIC_WRITE(비관적 락 모드) 사용
* API 로 테스트시 정상적으로 100개의 쿠폰만 발급 성공
* 테스트코드로 10개의 스레드풀로 1000명의 트래픽 발생시 커넥션 감당 못하고 에러 발생
* 유저 줄이고 hikari pool 늘려도 불안정
2. Redis redisson 분산락 사용
* API 로 테스트시 정상적으로 100개의 쿠폰만 발급 성공
* 테스트코드로 10개의 스레드풀로 1000명의 트래픽 발생시 정상적으로 100개의 쿠폰만 발급 성공

BackEnd : Springboot, Java17, JPA  
Infra: AWS EC2, MariaDB, ElastiCache(Redis)  
ETC: nGrinder, Docker, GitHub Actons

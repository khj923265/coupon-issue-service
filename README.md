# 선착순 쿠폰발급 서비스 구현

## 순식간에 많은 트래픽상황에서 정해진 쿠폰의 갯수 만큼만 발급되는 서비스 구현

## 간단한 아키텍처
<img width="1017" alt="image" src="https://github.com/khj923265/coupon-issue-service/assets/68458092/77caa3a7-9195-4e0b-b949-5ff79c9a8da5">


## 네고왕 보고 궁금해진 대규모 트래픽 동시성제어와 RDB VS Redis 차이 비교
1. MariaDB PESSIMISTIC_WRITE(비관적 락 모드) 사용
2. Redis redisson 분산락 사용

BackEnd : Springboot, Java17, JPA  
Infra: AWS EC2, MariaDB, ElastiCache(Redis)  
ETC: nGrinder, Docker, GitHub Actons

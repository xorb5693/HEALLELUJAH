#매칭 정보를 기록하기 위한 테이블입니다.
#쪽지 페이지의 자신의 계정과 쪽지를 주고받은 계정을 찾기 위한 기능으로도 이용이 됩니다.

CREATE TABLE matching (
  healthuserid varchar(20) NOT NULL,
  trainerid varchar(20) NOT NULL,
  PRIMARY KEY(healthuserid,trainerid),
  FOREIGN KEY(healthuserid) REFERENCES healthuser(id),
  FOREIGN KEY(trainerid) REFERENCES trainer(id)
)

INSERT INTO matching VALUES 
('test1','trainer2'),
('test1','trainer4'),
('test1','trainer6'),
('test1','trainer8');

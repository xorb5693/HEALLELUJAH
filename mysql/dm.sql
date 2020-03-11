#쪽지 기능을 위한 테이블입니다.

CREATE TABLE dm (
  contents varchar(1000) NOT NULL,
  senderid varchar(20) NOT NULL,
  receiverid varchar(20) NOT NULL,
  number int(11) NOT NULL,
  date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (senderid, receiverid, number),
  FOREIGN KEY (senderid) REFERENCES user(id),
  FOREIGN KEY (receiverid) REFERENCES user(id)
);

INSERT INTO dm VALUES 
('회원 test와(과) 트레이너 test이(가) 매칭되었습니다.','test1','trainer2',1,'2019-12-02 02:01:49'),
('테스트입니다','test1','trainer2',2,'2019-12-02 17:16:55'),
('test','test1','trainer2',3,'2019-12-02 22:26:34'),
('회원 test와(과) 트레이너 test이(가) 매칭되었습니다.','test1','trainer4',1,'2019-12-01 20:37:12'),
('테스트입니다','test1','trainer4',2,'2019-12-02 18:42:27'),
('안녕하세요','test1','trainer4',3,'2019-12-02 22:27:15'),
('회원 test와(과) 트레이너 test이(가) 매칭되었습니다.','test1','trainer6',1,'2019-12-01 20:37:19'),
('테스트2입니다','test1','trainer6',2,'2019-12-02 21:23:19'),
('회원 test와(과) 트레이너 test이(가) 매칭되었습니다.','test1','trainer8',1,'2019-11-28 04:58:42'),
('test1','test1','trainer8',2,'2019-12-01 20:41:14'),
('test2','test1','trainer8',3,'2019-12-01 20:41:21'),
('매칭입니다','trainer2','test1',2,'2019-12-02 17:26:20'),
('안녕하세요','trainer4','test1',4,'2019-12-02 22:28:27'),
('test3','trainer8','test1',4,'2019-12-01 20:41:37');

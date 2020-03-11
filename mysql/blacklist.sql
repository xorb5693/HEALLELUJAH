#블랙리스트 기능을 위한 DB입니다.
#미구현된 기능입니다.

CREATE TABLE blacklist (
  healthuserid varchar(20) NOT NULL,
  trainerid varchar(20) NOT NULL,
  PRIMARY KEY(healthuserid, trainerid),
  FOREIGN KEY(healthuserid) REFERENCES healthuser(id),
  FOREIGN KEY(trainerid) REFERENCES trainer(id)
);

#-*- coding: utf-8 -*-
import sys
import requests
import pprint
import difflib
import pymysql

def sim_str(str1, str2): 
    prsim = difflib.SequenceMatcher(None, str1, str2).ratio()
    return prsim

dic = {
	'린매스업' : 1, 
	'다이어트' : 2, 
	'체형교정' : 3, 
	'약점보완' : 4,
	'대회준비' : 5,
	'프로필사진' : 6,
	'재활교정' : 7,
	'멸치탈출' : 8,
	'선수준비' : 9
}

simresult = {

}

obj1 = dic[sys.argv[1]]
obj2 = dic[sys.argv[2]]
obj3 = dic[sys.argv[3]]
lat = sys.argv[4]
lng = sys.argv[5]
pay = int(sys.argv[6])
query = "select id, field1, field2, field3, pay from trainer where sqrt((power(((cos(latitude) * 6400 * 2 * 3.14 / 360) * abs((longtitude-" + str(lng) + "))), 2) + power((111 * abs(latitude - " + str(lat) + ")), 2))) < 3"

db = pymysql.connect(host = 'DB 주소', user='아이디', passwd='비밀번호', db='DB ', charset='utf8')
cur = db.cursor()
row = cur.execute(query)

while True : 
	rows = cur.fetchone() 
	if rows is None : break

	#python version이 2로 인식되어 최상단 UTF-8로 인식하는 기능과 sql 출력문의 encode가 필효함
	traobj1 = dic[rows[1].encode('UTF-8')]
	traobj2 = dic[rows[2].encode('UTF-8')]
	traobj3 = dic[rows[3].encode('UTF-8')]

	trasim = str(traobj1) + str(traobj1) + str(traobj1) + str(traobj2) + str(traobj2) + str(traobj3)
	usersim = str(obj1) + str(obj1) + str(obj1) + str(obj2) + str(obj2) + str(obj3)

	sim = sim_str(trasim, usersim)
	
	if pay > rows[4] :
		sim = sim + (rows[4] / pay)
	else :
		sim = sim + (pay / rows[4])
	simresult[rows[0]] = sim

sort_dic = sorted(simresult.items(), reverse = True, key=lambda item: item[1])


#for key, value in sort_dic :
#	print(key, ":", value)

for key, value in sort_dic : 
	print(key)

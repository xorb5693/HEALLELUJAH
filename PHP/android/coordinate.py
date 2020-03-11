#-*- coding: utf-8 -*-
import requests
import pprint
import sys

#파이썬 버전 2.7

#주소 초기화
address = ""

#주소가 띄어쓰기로 나눠져 있기 때문에 하나로 합쳐주는 작업 필요
for i in range(1, len(sys.argv)) :
  address = address + " " + sys.argv[i]

#[Key]에 Goolge 지도 API 키 필요
URL = 'https://maps.googleapis.com/maps/api/geocode/json?key=[Key]&sensor=false&language=ko&address=' + address

response = requests.get(URL)

#json으로 파싱하여 위도와 경도 추출
data = response.json()

lat = data['results'][0]['geometry']['location']['lat']
lng = data['results'][0]['geometry']['location']['lng']

#pprint.pprint(data)
#print(URL)
print(lat)
print(lng)

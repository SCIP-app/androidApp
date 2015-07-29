import random
import time 
import sys

random.seed()
partcipantList = []
for i in range(0, int(sys.argv[1])):
	partcipantList.append(590900100+100*i+random.randrange(1,9))

#f = open('viralload-'+time.strftime("%H-%M-%S")+'.txt', 'w')
f = open('viralload.txt', 'w')

for p in partcipantList:
	year = 2015
	month = random.randrange(7,13)
	numRecords = random.randrange(1,8)
	for i in range(0, numRecords):
		visitCode = random.randrange(1000, 9999)
		day = random.randrange(1, 28)
		if(numRecords/3 >= i) :
			load = random.randrange(45000, 120000)
		elif (numRecords*2/3 >= i):
			load = random.randrange(500, 8000)
		else :
			load = random.randrange(30, 150)
		f.write(str(p)+';'+str(visitCode)+';'+str(day)+"/"+str(month)+"/"+str(year)+";"+str(load)+'\n')
		month = month+3
		if(month>12) :
			month = month-12
			year = year + 1

f.close()

f = open('memscap.txt', 'w')

month30 = [9, 4, 6, 11]
month31 = [1, 3, 5, 7, 8, 10, 12]
partner_list = []
for p in partcipantList:
	partner_list.append(p+10)

for p in partner_list:
	memsId = random.randrange(100000,999999)
	year = 2015
	month = random.randrange(1,13)
	numRecords = random.randrange(20, 300)
	day = random.randrange(1,28) 
	hour = random.randrange(1,24)
	for i in range(0, numRecords) :
		minute = random.randrange(1, 60)
		f.write(str(p)+';'+str(memsId)+';'+str(day)+"/"+str(month)+"/"+str(year)+";"+str(hour)+':'+str(minute)+'\n')
		day = day+1
		if month in month30 and day > 30:
			day = 1
			month = month + 1
		elif month in month31 and day > 31: 
			day = 1
			month = month + 1
			if month == 13: 
				month = 1
				year = year + 1
		elif month == 2 and day > 28: 
			day = 1
			month = month + 1

female_list = []
male_list = []
for i in range(0, len(partcipantList)) :
	if random.randrange(1,3) == 1:
		female_list.append(partcipantList[i])
		male_list.append(partner_list[i])
	else :
		female_list.append(partner_list[i])
		male_list.append(partcipantList[i])

f = open('participants.txt', 'w')

for i in range(0, len(female_list)):
	f.write(str(female_list[i])+';1\n')
	f.write(str(male_list[i])+';0\n')

f.close()
f = open('surveyresults.txt', 'w')
for p in female_list:
	cycleLength = random.randrange(28, 37)
	periodLength = random.randrange(3, 8)
	lutealLength = random.randrange(12, 17)
	follicularLength = cycleLength - lutealLength
	numRecords = random.randrange(10, 300) 
	year = 2015
	month = random.randrange(7,13)
	day = random.randrange(1,29)
	cycleDay = 1
	for i in range(0, numRecords) :
		temperature = random.randrange(978, 989) / 10.0
		if cycleDay <= follicularLength :
			temperature = random.randrange(970, 978) / 10.0
		vaginaMucusSticky = 'false' 
		isOvulating = 'false'
		if cycleDay >= follicularLength-2 and cycleDay <= follicularLength :
			vaginaMucusSticky = 'true'
			isOvulating = 'true'
		onPeriod = 'false'
		if(cycleDay <= periodLength) :
			onPeriod = 'true'
		hadSex = 'false'
		usedCondom = 'true'
		if random.randrange(1,3) == 1:
			hadSex = 'true'
			usedCondom = 'true'
			if random.randrange(1,10) == 9:
				usedCondom = 'false'
		f.write(str(p)+';'+str(day)+"/"+str(month)+"/"+str(year)+";"+str(temperature)+';'+vaginaMucusSticky+';'+onPeriod+';'+isOvulating+';'+hadSex+';'+usedCondom+'\n')
		day = day+1
		if month in month30 and day > 30:
			day = 1
			month = month + 1
		elif month in month31 and day > 31: 
			day = 1
			month = month + 1
			if month == 13: 
				month = 1
				year = year + 1
		elif month == 2 and day > 28: 
			day = 1
			month = month + 1
		cycleDay = cycleDay + 1
		if cycleDay > cycleLength:
			cycleDay = 1
f.close()
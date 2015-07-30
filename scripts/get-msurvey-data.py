import requests 

url = 'https://apps.msurvey.co.ke/surveyapi/scip/data/?format=json'
headers = {'TOKEN':'PUT TOKEN HERE'}

r = requests.get(url, headers=headers)

print r.json()
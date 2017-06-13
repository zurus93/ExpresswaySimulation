import matplotlib.pyplot as plt
import csv
import sys
from os import listdir

for path in (f for f in listdir('logs')):
	data = [[], [], [], []]
	c=['black', 'green', 'blue', 'red']
	print(data)
	with open('logs/'+path, 'r') as csvfile:
		reader = csv.reader(csvfile, delimiter=',')
		for row in reader:
			print(row)
			for i, col in enumerate(data):
				data[i].append(row[i])

			
	for i in range(4):
		plt.plot(data[i], color=c[i])

	plt.savefig('img/'+path.split('.')[0])
	plt.gcf().clear()
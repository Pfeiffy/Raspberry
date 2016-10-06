# /usr/bin/env python
# -*- coding: utf-8 -*-
# studenten = [('Pet', 123), ('Paula', 988), ('Freddi', 851)]
#
import time

# for student in studenten:
 
x=1
while x>0:
	datei = open("test.dat", "w")
	datei.write(time.strftime("%X"))
	datei.close()

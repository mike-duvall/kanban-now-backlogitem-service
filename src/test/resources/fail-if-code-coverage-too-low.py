# Hack city.  The jacoco:check task is not working in gradle yet, so I've built this hard-coded super hacked
# Method of manually finding the percent code coverage in the generated html file
# This is hard-coded and brittle.  I'll replace it as soon as jacoco:check becomes available
# Reference: http://issues.gradle.org/browse/GRADLE-2854
import sys

f = open("../../../build/reports/jacoco/test/html/index.html")
lines = f.readlines()
f.close()
lastLine = lines[9]
indexOfPercent = lastLine.index('%')
indexOfClosedAngleBracket = lastLine.index('>')
totalCodeCoveragePercent = lastLine[indexOfClosedAngleBracket+1:indexOfPercent]
print "Total code coverage: " + totalCodeCoveragePercent
if int(totalCodeCoveragePercent) < 78:
    sys.exit(1)

# Hack city. The jacoco:check task is not working in gradle yet, so I've built this hard-coded super hacked
# Method of manually finding the percent code coverage in the generated html file
# This is hard-coded and brittle. I'll replace it as soon as jacoco:check becomes available
# Reference: http://issues.gradle.org/browse/GRADLE-2854
import sys

f = open("../../../build/reports/jacoco/test/html/index.html")
lines = f.readlines()
f.close()
lastLine = lines[0]
indexOfPercent = lastLine.index('%')
#indexOfClosedAngleBracket = lastLine.index('>')
indexOfClosedAngleBracket = lastLine.rfind('>', 0, indexOfPercent)
totalCodeCoveragePercent = lastLine[indexOfClosedAngleBracket+1:indexOfPercent]

print "Total code coverage: " + totalCodeCoveragePercent
requiredCodeCoverageLevel = 78
if int(totalCodeCoveragePercent) < int(requiredCodeCoverageLevel):
    print "Code coverage failed.  Required coverage:" + str(requiredCodeCoverageLevel)  + ",  Actual coverage:" + str(totalCodeCoveragePercent)
    sys.exit(1)

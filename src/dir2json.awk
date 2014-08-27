BEGIN {
	FS="/"
} 
{
	if(key != $1) {
		if(value) {
			print "	\"" value "\"],"
		}
		print "\"" $1 "\": ["
	}
} 
{ 
	key = $1
	value = $2;
}
value { 
	print "	\"" value "\","
} 
END { 
	print "	\"" value "\"]"
}

y=1
a=123
n=511
k=3
x=[0,0,1,0,1]

i = len(x)-1
while i != 0 :
    print(i)
    print("Start of for: " + str(y))
    y = (y*y) % n
    print("After first mod: " + str(y))
    if x[i] == 1:
        print("In if statement, so 1 bit is here ")
        y = (y*a) % n
        print("end of if: "+str(y))
    i=i-1
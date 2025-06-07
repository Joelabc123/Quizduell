people = [
    {"name": "Peter","age": 25,},
    {"name": "Petra","age": 27,},
    {"name": "Paulinaa","age": 19,},
]

older_than_twemty = []
for person in people:
    if person["age"] > 20:
        older_than_twemty.append({
            "name": person["name"],
            "age": person["age"]
        })

print(older_than_twemty)

def calc(my_number):
    return my_number * 2

print(calc(5))

text = "Welcome to the University of Mannheim"

def function(text):
    countA = 0
    countB = 0

    lowerText = text.lower()

    for char in lowerText:
        if char == 'a':
            countA+=1
        elif char == 'n':
            countB+=1
    dict = {
        "a" : countA,
        "b" : countB
    }

print(dict)
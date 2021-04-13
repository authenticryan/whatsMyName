# whatsMyName

**Inputs given** - Name, Gender, Year of Birth.

**Data Set** - US baby names dataset given by Social Security, CSV Format. 

**Business Logic** - Takes your name and looks it against the popularity index in your birth year. That popularity index helps understand your new name, if you were born in any other year, up to 2018. 

**Example** - My name is Ryan, born in 1997.  If Ryan was the 53rd most popular name in 1997, the code logic indicates that if I were born in 2011, I would be Mathew, as Mathew was the 53rd most popular name in 2011. 

**Additional Data Slices added** - Project expanded to include slices of data such as 
- Year the name was ranked the highest
- Average rank of the name amidst selected years
- Cumulative births logged before the current birth name, in ranking order

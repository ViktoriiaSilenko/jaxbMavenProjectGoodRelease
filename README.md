1.       Parse XML schema using JAXB

2.       Filter data

 
Conditions for filtering data:
1.       User type input string like: “field_name: param_name”, where field_name is field in XML schema.

2.       param_name  can be like full parameter or a part of it. F.e. “brand: Mercedess-Benz” (field_name is brand; param_name is Mercedess-Benz) or “brand: Merc”

3.       field_name can be represent in schema like: single word, two words separated by “-”, two or more words

4.       filter algorithm should ignore register

5.       filter algorithm should ignore multiply punctuation, f.e user type “brand: !@#$%$%^&*Mercedess-Benz” and algorithm should ignore “!@#$%$%^&*” all these characters and create search only by “brand: Mercedess-Benz”

6.       If field_name is price it algorithm should search by full price. F.e. at schema it is row like <price>$1.567</price>, for getting item by this field_name user should type “price: 1.567”, otherwise if user type “price: 567” it should show message that “According to searching param, no matches found”

7.       If searching string is empty app should show all items in XML

8.       Searching can be by several field_name, f.e. “brand: Mercedess price: 34.567 engine: 3.0” (where: brand, price, engine is field_name)

9.       App should ignore user typing errors such as: multiply spaces between field_name and param_name, missing “:” (f.e. “brand Mercedess”)

10.   If input string is without field_name or param_name app should show error message “Input string structure is incorrect”

11.   If no matches found app should show message “According to searching param, no matches found”

 
Condition for algorithm:
1.       Searching algorithm should be standalone for any XML schema

 

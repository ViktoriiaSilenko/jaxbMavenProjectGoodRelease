package ua.eurosoftware.jaxbmavenproject2.main;


import generated.Employee;
import generated.Employees;
import generated.Name;
import generated.Salary;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ua.eurosoftware.jaxbmavenproject2.filters.EmployeeFilter;


public class Main {

    private static String pathToXML = "src/main/resources/employees.xml";

    HashSet<String> fieldNamesSet = new HashSet<String>();

    public static StringWriter marshalling(JAXBContext jaxbContext, Employees employees) throws JAXBException {

        Employee newEmployee = new Employee();
        newEmployee.setId("3");
        newEmployee.setAge(new BigInteger("33"));
        newEmployee.setDepartment("QA");
        newEmployee.setPosition("Senior QA");
        Name name2 = new Name();
        name2.setFirstName("Stepan");
        name2.setLastName("Ivanov");
        newEmployee.setName(name2);
        Salary salary2 = new Salary();
        salary2.setValue(new BigDecimal(1000));
        salary2.setCurrency("dollars");
        newEmployee.setSalary(salary2);

        employees.getEmployee().add(newEmployee);

        //marshalling the object to xml
        StringWriter writer = new StringWriter();
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(employees, writer);
        //System.out.println(writer.toString());
        return writer;
    }

    public static Employees unmarshalling(JAXBContext jaxbContext, StringWriter writer) throws JAXBException {

        //unmarshalling the xml to object
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Employees employeesUnmarshalled = (Employees) jaxbUnmarshaller.unmarshal(new StringReader(writer.toString()));
        return employeesUnmarshalled;
    }

    public static Employees unmarshallingXML(JAXBContext jaxbContext) throws JAXBException {

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        File xml = new File(pathToXML);
        Employees employeeUnmarshalled = (Employees) jaxbUnmarshaller.unmarshal(xml);

        //unmarshalling the xml to object
        return employeeUnmarshalled;
    }

    public static void printEmployeeInformation(Employee employee) {

        Name name = employee.getName();
        System.out.println("first name: " + name.getFirstName());
        System.out.println("last name: " + name.getLastName());
        System.out.println("age: " + employee.getAge());
        System.out.println("position: " + employee.getPosition());
        System.out.println("department: " + employee.getDepartment());
        Salary salary = employee.getSalary();
        System.out.println("salary: " + salary.getValue() + " " + salary.getCurrency());
        System.out.println();
    }

    public static void printEmployeesInformation(List<Employee> employeesList) {

        //11. If no matches found app should show message “According to searching param, no matches found”
        if (employeesList.size() == 0) {
            System.out.println("According to searching param, no matches found");

        } else {
            for (Employee resultEmployee : employeesList) {
                printEmployeeInformation(resultEmployee);
            }
        }
    }

    public static String userInputHandle(BufferedReader bis) throws IOException {
        System.out.println("Please enter search string in format:");
        System.out.println("field_name: param_name");
        System.out.println("or field_name param_name");
        System.out.println("");

        String searchString = bis.readLine();
        // if (searchString.trim().matches("[.+\\s+.+]*")) {
        return searchString;

    }

    public static void search(String searchString) {

        JAXBContext jaxbContext;

        try {
            /* jaxbContext = JAXBContext.newInstance(Employees.class);

             // Create the XMLFilter
             XMLFilter filter = new EmployeeFilter();

             // Set the parent XMLReader on the XMLFilter
             SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
             SAXParser saxParser = saxParserFactory.newSAXParser();
             XMLReader xmlReader = saxParser.getXMLReader();
             filter.setParent(xmlReader);

             // Set UnmarshallerHandler as ContentHandler on XMLFilter
             Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
             UnmarshallerHandler unmarshallerHandler = jaxbUnmarshaller.getUnmarshallerHandler();
             filter.setContentHandler(unmarshallerHandler);

             // Parse the XML
             InputSource xml = new InputSource(pathToXML);
             filter.parse(xml);
             Employees employees = (Employees) unmarshallerHandler.getResult();

             Employee newEmployee = new Employee();
             newEmployee.setId("3");
             newEmployee.setAge(new BigInteger("33"));
             newEmployee.setDepartment("QA");
             newEmployee.setPosition("Senior QA");
             Name name2 = new Name();
             name2.setFirstName("Stepan");
             name2.setLastName("Ivanov");
             newEmployee.setName(name2);
             Salary salary2 = new Salary();
             salary2.setValue(new BigDecimal(1000));
             salary2.setCurrency("dollars");
             newEmployee.setSalary(salary2);

             employees.getEmployee().add(newEmployee);

             // Marshal the object to XML
             Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
             jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); */

            jaxbContext = JAXBContext.newInstance(new Class[]{Employees.class});

            Employees employees = unmarshallingXML(jaxbContext);

            StringWriter writer = marshalling(jaxbContext, employees);
            employees = unmarshalling(jaxbContext, writer);

            // 7. If searching string is empty app should show all items in XML
            if ("".equals(searchString)) {
                printEmployeesInformation(employees.getEmployee());
                // or we can write:
                //jaxbMarshaller.marshal(employees, System.out);
            } else {
                // 9. App should ignore user typing errors such as: 
                // multiply spaces between field_name and param_name, missing “:” (f.e. “brand Mercedess”)
                String[] words = searchString.split("\\s+|\\s*:\\s*");
                String fieldName = null;
                String fieldText = null;
                int first = 0;
                for (int i = 0; i < words.length; i++) {
                    if (!"".equals(words[i].trim().replaceAll("!|@|#|\\$|%|\\^|&|\\*", ""))) {
                        if (first == 0) {
                            //5. filter algorithm should ignore multiply punctuation, 
                            //f.e user type “brand: !@#$%$%^&*Mercedess-Benz” and algorithm should ignore “!@#$%$%^&*” all these characters 
                            //and create search only by “brand: Mercedess-Benz”
                            fieldName = words[i].trim().replaceAll("!|@|#|\\$|%|\\^|&|\\*", "");
                            first++;
                        } else {
                            fieldText = words[i].trim().replaceAll("!|@|#|\\$|%|\\^|&|\\*", "");
                        }
                    }
                }

                //System.out.println("fieldName = " + fieldName);
                //System.out.println("fieldText = " + fieldText);
                // 10. If input string is without field_name or param_name 
                // app should show error message “Input string structure is incorrect”
                if ((fieldName == null) || ("".equals(fieldName))
                        || (fieldText == null) || ("".equals(fieldText))) {
                    System.out.println("Input string structure is incorrect");
                } else {
                    List<Employee> employeesList = employees.getEmployee();
                    List<Employee> resultEmployeesList = new ArrayList<Employee>();

                    for (Employee employee : employeesList) {
                        // Employee has fields: firstName, lastName, age, position, department, salary 

                        //4. filter algorithm should ignore register
                        if ((Objects.equals("firstName".toLowerCase(), fieldName.toLowerCase())
                                || "firstName".toLowerCase().matches(".*" + fieldName.toLowerCase() + ".*"))
                                && (Objects.equals(employee.getName().getFirstName().toLowerCase(), fieldText.toLowerCase())
                                //2. param_name  can be like full parameter or a part of it. 
                                // F.e. “brand: Mercedess-Benz” (field_name is brand; param_name is Mercedess-Benz) or “brand: Merc”
                                || employee.getName().getFirstName().toLowerCase().matches(".*" + fieldText.toLowerCase() + ".*"))) {

                            resultEmployeesList.add(employee);
                        } else if ((Objects.equals("lastName".toLowerCase(), fieldName.toLowerCase())
                                || "lastName".toLowerCase().matches(".*" + fieldName.toLowerCase() + ".*"))
                                && (Objects.equals(employee.getName().getLastName().toLowerCase(), fieldText.toLowerCase())
                                || employee.getName().getLastName().toLowerCase().matches(".*" + fieldText.toLowerCase() + ".*"))) {

                            resultEmployeesList.add(employee);
                        } else if ((Objects.equals("age", fieldName.toLowerCase())
                                || "age".matches(".*" + fieldName.toLowerCase() + ".*"))
                                && Objects.equals(employee.getAge().toString(), fieldText)) {

                            resultEmployeesList.add(employee);
                        } else if ((Objects.equals("position", fieldName.toLowerCase())
                                || "position".matches(".*" + fieldName.toLowerCase() + ".*"))
                                && (Objects.equals(employee.getPosition().toLowerCase(), fieldText.toLowerCase())
                                || employee.getPosition().toLowerCase().matches(".*" + fieldText.toLowerCase() + ".*"))) {

                            resultEmployeesList.add(employee);
                        } else if ((Objects.equals("department", fieldName.toLowerCase())
                                || "department".matches(".*" + fieldName.toLowerCase() + ".*"))
                                && (Objects.equals(employee.getDepartment().toLowerCase(), fieldText.toLowerCase())
                                || employee.getDepartment().toLowerCase().matches(".*" + fieldText.toLowerCase() + ".*"))) {

                            resultEmployeesList.add(employee);
                        } else if ((Objects.equals("salary", fieldName.toLowerCase())
                                || "salary".matches(".*" + fieldName.toLowerCase() + ".*"))
                                //If field_name is price it algorithm should search by full price. 
                                //F.e. at schema it is row like <price>$1.567</price>, 
                                //for getting item by this field_name user should type “price: 1.567”, 
                                //otherwise if user type “price: 567” it should show message 
                                //that “According to searching param, no matches found”
                                && Objects.equals(employee.getSalary().getValue().toString(), fieldText)) {

                            resultEmployeesList.add(employee);
                        }

                    }

                    printEmployeesInformation(resultEmployeesList);
                }
            }

        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader bis = new BufferedReader(is);
        String searchString = null;

        try {
            int number = -1;
            int counter = 0;
            while (number < 0) {
                System.out.println("Please, enter number of searching:");
                System.out.println();

                try {
                    number = Integer.parseInt(bis.readLine());
                } catch (NumberFormatException ex) {
                    System.out.println("You enter not an integer number. Please, enter integer positive number");
                }

            }
            if (number > 0) {

                searchString = userInputHandle(bis);
                counter++;

                if (searchString != null) {
                    search(searchString.trim());
                }

                while (counter < number) {
                    searchString = userInputHandle(bis);
                    counter++;

                    if (searchString != null) {
                        search(searchString.trim());
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("Input error: " + e);
        }

    }

}

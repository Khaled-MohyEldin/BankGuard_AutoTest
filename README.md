# 💼 BankGuard_AutoTest

**Automated Testing Framework for Banking APIs and Database Validation**

BankGuard_AutoTest is a lightweight yet scalable automation framework built to validate banking APIs and database operations with precision and clarity. Designed for QA engineers and developers who value clean architecture and reliable test coverage, this project integrates API and DB testing workflows using tools like RestAssured, POSTMAN, and TestNG.

## 🚀 Features

- ✅ API Testing with RestAssured and POSTMAN collections  
- 🗄️ Database Validation using JDBC queries  
- 🔁 Regression & Smoke Testing support  
- 🧪 TestNG Integration for structured test execution  
- 🔄 CI/CD Ready with extensible architecture for Jenkins pipelines  
- 📄 Readable & Maintainable Codebase following POM design principles

## 🛠️ Tech Stack

| Layer            | Tools Used              |
|------------------|--------------------------|
| API Automation   | RestAssured, POSTMAN     |
| DB Validation    | JDBC                     |
| Test Framework   | TestNG                   |
| Build Tool       | Maven                    |
| Language         | Java                     |
| CI/CD            | Jenkins (optional setup) |

## 📁 Project Structure

BankGuard_AutoTest/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── core/         # Core utilities and base classes
│   └── test/
│       └── java/
│           └── tests/        # API and DB test cases
├── pom.xml                   # Maven configuration
├── testng.xml                # TestNG suite config
└── README.md                 # Project documentation

## 🧭 Getting Started
0. get Test target from awesome friend Shady
   `git clone https://github.com/Shady1997/-Mobile_Bank_API_RestFul`

2. Clone the repo  
   `git clone https://github.com/Khaled-MohyEldin/BankGuard_AutoTest.git`

3. Install dependencies  
   Run `mvn clean install` to set up the project.

4. Configure DB & API endpoints  
   Update config files with your environment details.

5. Run tests  
   Use TestNG or Maven commands to execute the suite.

## 📌 Future Enhancements

- 🔐 Security testing integration  
- 📊 Test reporting with Allure or ExtentReports  
- 🌐 Environment-based config management  
- 🧩 Dockerized test execution

## 👨‍💻 Author

**Khaled MohyEldin**  
Software QC Engineer | Automation Specialist  
[LinkedIn Profile](https://www.linkedin.com/in/khaled-mohyeldin-07271285/)

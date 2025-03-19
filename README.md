# JavaFX Accounting System

A desktop accounting system built with JavaFX, featuring voucher management, ledger system, and financial reporting.

## Development Environment

### Required Tools

- JDK 23
- Maven
- IntelliJ IDEA (recommended)
- Scene Builder (for FXML editing)

### Project Setup

1. Clone the repository
2. Open in IntelliJ IDEA
3. Ensure JDK 23 is configured
4. Run `mvn clean install`
5. Run `AccountingSystemApplication.java`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/accounting/system/
│   │       ├── AccountingSystemApplication.java  # Main application
│   │       ├── controller/                       # UI controllers
│   │       ├── model/                           # Data models
│   │       └── service/                         # Business logic
│   └── resources/
│       ├── styles.css                           # Application styles
│       └── fxml/                                # UI layout files
```

## Core Modules

### 1. Main Interface

- Menu bar (File, Edit, Help)
- Sidebar navigation
- Tab-based content area

### 2. Voucher Management

- Voucher listing with TableView
- CRUD operations (Add, Edit, Delete)
- Verification functionality
- Columns:
  - Voucher No.
  - Date
  - Description
  - Debit Amount
  - Credit Amount
  - Status

### 3. Ledger System

- General ledger view
- Account management
- Transaction tracking

### 4. Settings

- Basic system settings
- User preferences
- System configuration

## Development Workflow

1. **Setup**

   - Clone repository
   - Configure JDK 17
   - Import Maven dependencies

2. **Development**

   - Create feature branch
   - Implement UI using FXML/JavaFX
   - Add business logic
   - Style with CSS

3. **Testing**

   - Run application locally
   - Test all UI interactions
   - Verify data operations

4. **Deployment**
   - Build with Maven
   - Package application
   - Create release

## Quick Start

```bash
# Clone repository
git clone [repository-url]

# Build project
mvn clean install

# Run application
mvn javafx:run
```

## Notes

- Application uses JavaFX for UI
- Styling is managed through styles.css
- Main window size: 1024x768
- Responsive layout with BorderPane

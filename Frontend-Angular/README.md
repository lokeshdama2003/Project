# Gather & Grow – Community Event Operations Portal

A practical Angular 20 project for managing local community events. It uses a different domain from the Employee/Student Management application.

## What each role can do

- **Admin:** Create events, view the event list, edit events and delete events.
- **Organizer:** View and search the community event schedule.
- **Attendee:** Browse and search events whose status is `Open`.

The login screen is a demo role selector for practising route guards. It is not real authentication. A production application must authenticate users and enforce permissions on the backend too.

## Angular topics used

- Standalone components and Angular routing
- `@Input()` and `@Output()` in reusable components
- `ngOnInit()` and `ngOnChanges()` lifecycle hooks
- `CanActivate` and `CanDeactivate` route guards
- Reactive Forms and form validation
- Services, dependency injection, HttpClient and Observables
- Custom filter pipe
- Reusable table and button components
- Bootstrap styling
- JSON Server CRUD operations
- Searching, sorting and dashboard summary cards

## Project requirements

- Node.js 20.19 or later
- npm
- Visual Studio Code (or another editor)

## Open and run the project

1. Extract the ZIP file.
2. Open the extracted `Community_Event_Operations_Portal` folder in VS Code.
3. Open the VS Code terminal and install the packages:

   ```bash
   npm install
   ```

4. Start JSON Server in Terminal 1:

   ```bash
   npm run api
   ```

   Keep this terminal running. The API is available at `http://localhost:3000/events`.

5. Open a second terminal and start Angular:

   ```bash
   npm start
   ```

6. Open `http://localhost:4200` in your browser.

## Try the application

1. Choose **Admin** and create a community event.
2. Add an event title, category, date, venue, capacity and status.
3. Edit the event and then try deleting it.
4. Search for an event and click a table heading to change the sort order.
5. Log out and choose **Organizer** to view the schedule.
6. Log out and choose **Attendee**. Only events with status `Open` should appear.
7. As Admin, change a form field and try navigating away to see the unsaved-changes warning.

## Build check

Run this command to check the Angular application locally:

```bash
npm run build
```

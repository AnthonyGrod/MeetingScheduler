# Meeting Scheduler
Program above takes as input meeting duration time (for example "00:30" means half an hour) and two JSON strings that represent a schedule. If you'd like to put JSON directly in source file you can do that for example like:
```
String json1 = "{\"working_hours\":{\"start\":\"09:00\",\"end\":\"19:55\"},\"planned_meeting\":[{\"start\":\"09:00\",\"end\":\"10:30\"},{\"start\":\"12:00\",\"end\":\"13:00\"},{\"start\":\"16:00\",\"end\":\"18:00\"}]}";
String json2 = "{\"working_hours\":{\"start\":\"10:00\",\"end\":\"18:30\"},\"planned_meeting\":[{\"start\":\"10:00\",\"end\":\"11:30\"},{\"start\":\"12:30\",\"end\":\"14:30\"},{\"start\":\"14:30\",\"end\":\"15:00\"},{\"start\":\"16:00\",\"end\":\"17:00\"}]}";
```

As an output the program prints out available time slots for a meeting. In the example above it would be:
```
[[11:30, 12:00], [15:00, 16:00], [18:00, 18:30]]
```

# Dependencies
In order to run the program GSON module (google.code.gson) is required.

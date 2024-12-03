#!/bin/bash

# Array of UUIDs for devices
DEVICE_IDS=("c61082c0-5329-4d1c-890f-2359ec976c8d" "e957cecd-63f9-4058-bb61-69c3af089cf3")
JAR_PATH="target/simulator_service-1.0-SNAPSHOT.jar"
PID_FILE="simulator_pids.txt"

# Clear old PID file
> "$PID_FILE"

# Start Java processes for each DEVICE_ID
for DEVICE_ID in "${DEVICE_IDS[@]}"; do
    java -jar "$JAR_PATH" "$DEVICE_ID" &
    echo $! >> "$PID_FILE"  # Save the PID
done

echo "Simulators started. PIDs saved to $PID_FILE."
echo "Press 'q' and Enter to stop all simulators."

# Monitor for user input
while true; do
    read -r -n 1 key
    if [[ $key == "q" ]]; then
        echo -e "\nStopping simulators..."
        # Stop all processes using PIDs
        if [ -f "$PID_FILE" ]; then
            while read -r pid; do
                kill "$pid" && echo "Stopped process $pid"
            done < "$PID_FILE"
            rm "$PID_FILE"
        fi
        echo "All simulators stopped."
        break
    fi
done

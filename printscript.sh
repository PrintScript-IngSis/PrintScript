#!/bin/bash

# Function to print usage instructions
print_usage() {
  echo "Usage: $0 [OPTIONS] INPUT_FILE [OPTIONAL_OUTPUT_FILE]"
  echo "Options:"
  echo "  -r, --run            Run interpreter"
  echo "  -l, --linter         Run linter"
  echo "  -f, --format         Run formatter"
  echo "  --rules=RULES_FILE   Use custom rules for formatter or linter (optional json file)"
  echo "  -h, --help           Show this message and exit"
}

# Check if no arguments are provided
if [ $# -eq 0 ]; then
  echo "Error: No arguments provided."
  print_usage
  exit 1
fi

# Initialize options
run_option=""
linter_option=""
format_option=""
rules_file_option=""
input_file=""
output_file=""

# Parse command line arguments
while [[ "$#" -gt 0 ]]; do
  case $1 in
    -r|--run)
      if [ -n "$run_option" ]; then
        echo "Error: Only one of -r, -l, or -f can be specified."
        print_usage
        exit 1
      fi
      run_option="-r"
      ;;
    -l|--linter)
      if [ -n "$linter_option" ]; then
        echo "Error: Only one of -r, -l, or -f can be specified."
        print_usage
        exit 1
      fi
      linter_option="-l"
      ;;
    -f|--format)
      if [ -n "$format_option" ]; then
        echo "Error: Only one of -r, -l, or -f can be specified."
        print_usage
        exit 1
      fi
      format_option="-f"
      ;;
    --rules=*)
      rules_file_option="--rules=${1#*=}"
      ;;
    -h|--help)
      print_usage
      exit 0
      ;;
    *)
      # Treat any other arguments as files
      if [ -z "$input_file" ]; then
        input_file="$1"
      elif [ -z "$output_file" ]; then
        output_file="$1"
      else
        echo "Error: Unexpected argument $1"
        print_usage
        exit 1
      fi
      ;;
  esac
  shift
done

# Check if input file is provided
if [ -z "$input_file" ]; then
  echo "Error: No input file provided."
  print_usage
  exit 1
fi

# Run the Kotlin code with the specified options
./gradlew :cli:run --args="$run_option $linter_option $format_option $rules_file_option $input_file $output_file"

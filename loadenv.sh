# Load revcrm.env file into environment
export $(cat revcrm.env | sed 's/#.*//g' | xargs)
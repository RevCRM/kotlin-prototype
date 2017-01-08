# revcrm
RevCRM - Open-source CRM solution built on RevJS

This repository holds just the RevCRM Server and base UI. No data models are included.

## Requirements

* NodeJS 6
* Yarn package manager (`npm install yarn -g`)

## Installation

```
mkdir my_crm
cd my_crm
yarn add revcrm
yarn run revcrm
```

## Customising

To cusomise your installation, create `crm.json` with content as below:

```json
{
    "db": {
        "driver": "mysql",
        "host": "127.0.0.1",
        "port": 1433,
        "user": "root"
    },
    "modules": [
        "my_custom_module"
    ]
}
```

You can install the standard RevCRM modules, for example the calendar module, by simply doing:

```
yarn add revcrm-calendar
```
...then adding them to the "modules" list in `crm.json`
#!/usr/bin/env node

const [nodecmd, script, operation] = process.argv;

switch (operation) {
    case 'start':
        console.log('Starting RevCRM...');
        break;
    default:
        console.log(`Command ${operation} is not recognised.`);
}

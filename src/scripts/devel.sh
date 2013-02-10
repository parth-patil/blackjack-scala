#!/bin/bash
echo "Starting blackjack in development mode..."
java -server -Xmx1024m -Dstage=development -jar ./dist/blackjack/@DIST_NAME@-@VERSION@.jar

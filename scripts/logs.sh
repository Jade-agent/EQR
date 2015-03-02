#! /bin/bash
# (2015) nelson kigen <nellyk89@gmail.com>
# View LOG files for the EQR Agents
# Assumes you have Google Chrome on your system

#set -x

ROOT="/home/nkigen/development/git/EQR/logs"

main() {
    cd ${ROOT}
    google-chrome-stable `ls *html`
}
main

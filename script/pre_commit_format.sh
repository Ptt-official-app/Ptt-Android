#!/usr/bin/env sh
mkdir .git/hooks/
echo "#!/usr/bin/env sh
#
# An example hook script to verify what is about to be committed.
# Called by \"git commit\" with no arguments.  The hook should
# exit with non-zero status after issuing an appropriate message if
# it wants to stop the commit.
#
# To enable this hook, rename this file to \"pre-commit\".
./gradlew formatCheck
result=\$?
SLASH='"\\"'
RED='"\033[31m"'
GREEN='"\033[32m"'
NC='"\033[0m"'
printf \"the spotlessCheck result code is \$result\"
if [[ \"\$result\" = 0 ]] ; then
    printf \"\$SLASH\$GREEN
    ....
    ....
    SpotlessCheck Pass!!
    ....
    ....
    \$SLASH\$NC\"
    exit 0
else
    ./gradlew format
    printf \"\$SLASH\$RED
    ....
    ....
    SpotlessCheck Failed!!
    There are some format violations in the commit files.
    ....
    Already auto format files, please review the difference and commit again.
    ....
    ....
    \$SLASH\$NC\"
    exit 1
fi
" >> .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit
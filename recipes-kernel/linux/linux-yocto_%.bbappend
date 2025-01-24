FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/openharmony:"

SRC_URI += "file://0001-OpenHarmony-4.1-Release-adaptation.patch"
SRC_URI += "file://0001-fix-access_tokenid-resolve-strict-prototypes-warning.patch"
SRC_URI += "file://0001-fixing-get_fs-set_fs-functions-undeclared.patch"

SRC_URI += " file://openharmony.cfg"
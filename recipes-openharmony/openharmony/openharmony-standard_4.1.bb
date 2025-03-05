# SPDX-FileCopyrightText: Huawei Inc.
#
# SPDX-License-Identifier: Apache-2.0

SUMMARY = "OpenHarmony 4.1"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS:prepend := "${THISDIR}/openharmony:"

inherit logging
inherit systemd

SRC_URI = "file:///home/francesco/Desktop/ohos-rootfs.tar;subdir=${BP}"
SRC_URI += "file://config"
SRC_URI += "file://ohos.service"

RPROVIDES:${PN} += " libinit_module_engine.so()(64bit)"

do_install () {
    OHOS_PACKAGE_OUT_DIR="${B}"
    bbnote "installing contents from ${OHOS_PACKAGE_OUT_DIR} to ${D}"

    mkdir -p ${D}/openharmony
    cp -r  ${OHOS_PACKAGE_OUT_DIR}/* ${D}/openharmony

    # copy lxc config file
    mkdir -p ${D}/var/lib/lxc/openharmony
    cp ${WORKDIR}/config ${D}/var/lib/lxc/openharmony/

    # copy systemd service file
    mkdir -p ${D}${systemd_system_unitdir}
    install -m 644 ${WORKDIR}/ohos.service ${D}${systemd_system_unitdir}/

    return 0
}

# OpenHarmony libraries are not versioned properly.
# Move the unversioned .so files to the primary package.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "openharmony"
FILES:${PN} += "${systemd_system_unitdir}/ohos.service"

EXCLUDE_FROM_SHLIBS = "1"

# To avoid excessive diskspace blowup, we are stripping our executables
INSANE_SKIP:${PN} += "already-stripped"

# Need this to allow libnative_window.so and libnative_drawing.so symlinks
INSANE_SKIP:${PN} += "dev-so"

SYSTEMD_SERVICE:${PN} = "ohos.service"

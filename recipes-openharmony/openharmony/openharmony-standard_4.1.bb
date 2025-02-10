# SPDX-FileCopyrightText: Huawei Inc.
#
# SPDX-License-Identifier: Apache-2.0

SUMMARY = "OpenHarmony 4.1"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS:prepend := "${THISDIR}/openharmony:"

inherit logging

SRC_URI = "file:///home/francesco/Desktop/ohos-rootfs.tar.gz;subdir=${BP}"
SRC_URI += "file://config"

RPROVIDES:${PN} += " libinit_module_engine.so()(64bit)"

do_install () {
    OHOS_PACKAGE_OUT_DIR="${B}"
    bbnote "installing contents from ${OHOS_PACKAGE_OUT_DIR} to ${D}"

    mkdir -p ${D}/openharmony
    cp -r  ${OHOS_PACKAGE_OUT_DIR}/* ${D}/openharmony

    # copy lxc config file
    mkdir -p ${D}/var/lib/lxc/openharmony
    cp ${WORKDIR}/config ${D}/var/lib/lxc/openharmony/

    return 0
}

# OpenHarmony libraries are not versioned properly.
# Move the unversioned .so files to the primary package.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "openharmony"


EXCLUDE_FROM_SHLIBS = "1"

# To avoid excessive diskspace blowup, we are stripping our executables
INSANE_SKIP:${PN} += "already-stripped"

# Need this to allow libnative_window.so and libnative_drawing.so symlinks
INSANE_SKIP:${PN} += "dev-so"

# TEMPORARY fix to: `do_package_qa: QA Issue: /lib/init/reboot/librebootmodule.z.so 
# contained in package openharmony-standard requires libinit_module_engine.so()(64bit), 
# but no providers found in RDEPENDS:openharmony-standard? [file-rdeps]`
#INSANE_SKIP:${PN} += "file-rdeps"

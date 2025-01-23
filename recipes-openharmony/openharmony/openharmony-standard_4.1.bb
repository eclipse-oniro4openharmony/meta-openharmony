# SPDX-FileCopyrightText: Huawei Inc.
#
# SPDX-License-Identifier: Apache-2.0

SUMMARY = "OpenHarmony 4.1"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS:prepend := "${THISDIR}/openharmony-${OPENHARMONY_VERSION}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/openharmony-standard-${OPENHARMONY_VERSION}:"

inherit logging

SRC_URI = "file:///home/francesco/Desktop/ohos-rootfs.tar.gz;subdir=${BP}"

RPROVIDES:${PN} += " libinit_module_engine.so()(64bit)"

do_install () {
    OHOS_PACKAGE_OUT_DIR="${B}"
    bbnote "installing contents from ${OHOS_PACKAGE_OUT_DIR} to ${D}"

    # We install library files to /lib and executables into /bin, and
    # then setup /system/lib and /system/bin symlinks to avoid breaking use of
    # hard-coded paths.
    mkdir -p ${D}/system ${D}/lib ${D}/bin
    cp -r ${OHOS_PACKAGE_OUT_DIR}/system/lib/* ${D}/lib/
    [ -d "${OHOS_PACKAGE_OUT_DIR}/system/lib64" ] && cp -r ${OHOS_PACKAGE_OUT_DIR}/system/lib64/* ${D}/lib/
    cp -r ${OHOS_PACKAGE_OUT_DIR}/system/bin/* ${D}/bin/
    find ${D}/bin/ -type f -exec chmod 755 {} \;
    ln -sfT ../lib ${D}/system/lib
    [ -d "${OHOS_PACKAGE_OUT_DIR}/system/lib64" ] && ln -sfT ../lib ${D}/system/lib64
    ln -sfT ../bin ${D}/system/bin

    # OpenHarmony etc (configuration) files
    mkdir -p ${D}${sysconfdir}
    cp -r  ${OHOS_PACKAGE_OUT_DIR}/system/etc/* ${D}${sysconfdir}
    ln -sfT ..${sysconfdir} ${D}/system/etc

    # system ability configurations
    mkdir -p ${D}/system/profile
    cp -r ${OHOS_PACKAGE_OUT_DIR}/system/profile/* ${D}/system/profile

    # copy /system/usr
    mkdir -p ${D}/system/usr
    [ -d "${OHOS_PACKAGE_OUT_DIR}/system/usr" ] && cp -r ${OHOS_PACKAGE_OUT_DIR}/system/usr/* ${D}/system/usr

    # OpenHarmony font files
    mkdir -p ${D}/system/fonts
    [ -d "${OHOS_PACKAGE_OUT_DIR}/system/fonts" ] && cp -r ${OHOS_PACKAGE_OUT_DIR}/system/fonts/* ${D}/system/fonts

    # OpenHarmony app files
    mkdir -p ${D}/system/app
    [ -d "${OHOS_PACKAGE_OUT_DIR}/system/app" ] && cp -r ${OHOS_PACKAGE_OUT_DIR}/system/app/* ${D}/system/app

    # copy /vendor
    [ -d "${OHOS_PACKAGE_OUT_DIR}/vendor" ] && mkdir -p ${D}/vendor
    [ -d "${OHOS_PACKAGE_OUT_DIR}/vendor" ] && cp -r  ${OHOS_PACKAGE_OUT_DIR}/vendor/* ${D}/vendor

    # initialize root file system 
    cd ${D}
    mkdir  chip_prod  config  data  dev  eng_chipset  eng_system  \
        mnt  module_update  proc  storage  sys  sys_prod  tmp  updater 
    ln -sf /vendor ${D}/chipset
    ln -sf /system/bin/init ${D}/init

    # exclude some libs and bins because conflicting with other yocto packages
    rm ${D}/bin/sh
    [ -d "${D}/etc/profile" ] && rm -r ${D}/etc/profile
    [ -d "${D}/etc/udev" ] && rm -r ${D}/etc/udev

    # when building for rk3568 target there is a binary in this directory that 
    # is built for aarch64 architecture when we are targeting arm
    [ -d "${D}/lib/module/arkcompiler" ] && rm -r ${D}/lib/module/arkcompiler

    # rename musl to avoid conflict with yocto provided libc
    mv ${D}/lib/ld-musl-aarch64.so.1 ${D}/lib/ohos-ld-musl-aarch64.so.1
    mv ${D}/etc/ld-musl-aarch64.path ${D}/etc/ohos-ld-musl-aarch64.path

    return 0
}

# OpenHarmony libraries are not versioned properly.
# Move the unversioned .so files to the primary package.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "\
    /system/* \
    /vendor/* \
    /lib/* \
    chip_prod  chipset  config  data  dev  eng_chipset  eng_system  \
    init  mnt  module_update  proc  storage  sys  sys_prod  tmp  updater \
"


EXCLUDE_FROM_SHLIBS = "1"

# To avoid excessive diskspace blowup, we are stripping our executables
INSANE_SKIP:${PN} += "already-stripped"

# Need this to allow libnative_window.so and libnative_drawing.so symlinks
INSANE_SKIP:${PN} += "dev-so"

# TEMPORARY fix to: `do_package_qa: QA Issue: /lib/init/reboot/librebootmodule.z.so 
# contained in package openharmony-standard requires libinit_module_engine.so()(64bit), 
# but no providers found in RDEPENDS:openharmony-standard? [file-rdeps]`
#INSANE_SKIP:${PN} += "file-rdeps"

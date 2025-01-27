FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/openharmony:"

SRC_URI += "file://0001-OpenHarmony-4.1-Release-adaptation.patch"
SRC_URI += "file://0001-fix-access_tokenid-resolve-strict-prototypes-warning.patch"
SRC_URI += "file://0001-fixing-get_fs-set_fs-functions-undeclared.patch"
SRC_URI += "file://kbuild_cflags.patch"

SRC_URI += " file://openharmony.cfg"

ORIGIN_URL = "git://github.com/eclipse-oniro-mirrors"

SRC_URI += "${ORIGIN_URL}/kernel_linux_common_modules.git;protocol=https;branch=OpenHarmony-4.1-Release;type=kmeta;name=kernel_linux_common_modules;destsuffix=kernel/linux/common_modules"
SRCREV_kernel_linux_common_modules = "84aec14ca5fa4afa90f6023118db1e6db9c35797"

SRC_URI += "${ORIGIN_URL}/third_party_bounds_checking_function.git;protocol=https;branch=OpenHarmony-4.1-Release;name=third_party_bounds_checking_function;destsuffix=third_party/bounds_checking_function"
SRCREV_third_party_bounds_checking_function = "dd8125bec1f8d9f28b7a9c99931d4b0580b7f78d"

SRC_URI += "${ORIGIN_URL}/third_party_FreeBSD.git;protocol=https;branch=OpenHarmony-4.1-Release;name=third_party_FreeBSD;destsuffix=third_party/FreeBSD"
SRCREV_third_party_FreeBSD = "a41e29c7838df28b020993abc714d81e0d0da83d"

SRC_URI += "${ORIGIN_URL}/drivers_hdf_core.git;protocol=https;branch=OpenHarmony-4.1-Release;name=drivers_hdf_core;destsuffix=drivers/hdf_core"
SRCREV_drivers_hdf_core = "4a9f9088e087e2999f81118f34792d3cbe8c87dd"

SRC_URI += "git://github.com/eclipse-oniro4openharmony/vendor_oniro.git;protocol=https;branch=OpenHarmony-4.1-Release;name=vendor_oniro;destsuffix=vendor/oniro"
SRCREV_vendor_oniro = "ffd56f100d5a2cd6e449dfa9ef74b4ba9067bcef"

SRC_URI += "file://hdf.patch"
SRC_URI += "file://hdf_patch.sh"

do_patch:append(){
    KERNEL_BUILD_ROOT=${S}
    bash ${WORKDIR}/hdf_patch.sh ${WORKDIR} ${KERNEL_BUILD_ROOT}
}

do_configure:prepend() {
    KERNEL_BUILD_ROOT=${S}
    ROOT_DIR=${WORKDIR}
    
    bash ${ROOT_DIR}/kernel/linux/common_modules/newip/apply_newip.sh ${ROOT_DIR} ${KERNEL_BUILD_ROOT} ${MACHINE} ${LINUX_VERSION}
    bash ${ROOT_DIR}/kernel/linux/common_modules/qos_auth/apply_qos_auth.sh ${ROOT_DIR} ${KERNEL_BUILD_ROOT} ${MACHINE} ${LINUX_VERSION}
    if [ ! -d "$KERNEL_BUILD_ROOT/security/xpm" ]; then
        bash ${ROOT_DIR}/kernel/linux/common_modules/xpm/apply_xpm.sh ${ROOT_DIR} ${KERNEL_BUILD_ROOT} ${MACHINE} ${LINUX_VERSION}
    fi
}

do_compile:prepend() { 
    export PRODUCT_PATH=vendor/oniro/x23

    # Find the path of the g++ compiler
    GPP_PATH=$(which g++)
    if [ -z "$GPP_PATH" ]; then
        bberr "g++ not found."
        exit 1
    fi

    # Get the directory where g++ is located
    GPP_DIR=$(dirname "$GPP_PATH")

    # Check if c++ exists in the same directory
    if [ ! -f "$GPP_DIR/c++" ]; then
        # If c++ does not exist, create a symlink to g++
        ln -s "$GPP_PATH" "$GPP_DIR/c++"
        bbnote "Symbolic link for c++ created."
    fi

}

do_compile_kernelmodules:prepend() {
    export PRODUCT_PATH=vendor/oniro/x23
}

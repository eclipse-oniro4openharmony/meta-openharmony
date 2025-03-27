# meta-openharmony
This README file contains information on the contents of the meta-openharmony layer.

Please see the corresponding sections below for details.

# Dependencies

This layer depends on the following Yocto meta-layers:

```
URI: poky/meta
branch: kirkstone

URI: poky/meta-poky
branch: kirkstone

URI: poky/meta-yocto-bsp
branch: kirkstone

URI: meta-openembedded/meta-oe
branch: kirkstone

URI: meta-openembedded/meta-filesystems
branch: kirkstone

URI: meta-openembedded/meta-python
branch: kirkstone

URI: meta-openembedded/meta-networking
branch: kirkstone

URI: meta-openharmony
branch: kirkstone

URI: meta-virtualization
branch: kirkstone
```

# Adding the meta-openharmony layer to your build

To add the meta-openharmony layer to your Yocto build, run the following command:

```
bitbake-layers add-layer meta-openharmony
```

# Environment Setup

You must define the `OHOS_DIR` environment variable, either in your `local.conf` file or as an environment variable in your shell.

`OHOS_DIR` should point to the root directory of your OpenHarmony build environment.

Example (in local.conf):

```
OHOS_DIR = "/path/to/openharmony"
```

# Features

This layer includes:

- Patches for the Linux kernel to support OpenHarmony.
- LXC container configuration files to launch the OpenHarmony system.
- A recipe to package the OpenHarmony root filesystem (rootfs) into a deployable image.

# Building

Before building the Yocto image, you need to generate the OpenHarmony rootfs tarball using the OpenHarmony build system.

Run the following command from within the OpenHarmony source directory:

`./build.sh --product-name qemu --build-target rootfs_pkg`

This will create a tarball containing the OpenHarmony rootfs, which is used by this meta-layer during the Yocto build process.

## Building the OpenHarmony Yocto Image

To build the OpenHarmony Yocto image, run the following command:

```
DISTRO=openharmony MACHINE=qemuarm64 bitbake openharmony-standard-image
```

This will generate the OpenHarmony image for the specified machine and distribution.

## Running the OpenHarmony QEMU Image

After building the OpenHarmony Yocto image, you can run it using QEMU with the following command:

```
DISTRO=openharmony MACHINE=qemuarm64 runqemu openharmony-standard-image serialstdio
```

This will launch the OpenHarmony image in QEMU with serial I/O redirected to the terminal.


# Misc

This layer enables integration of OpenHarmony as a guest containerized system in a Yocto-based environment, suitable for embedded and virtualization use cases.

Make sure all dependencies are properly added and `OHOS_DIR` is correctly configured before starting the build process.


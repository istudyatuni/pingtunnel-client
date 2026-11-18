{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  };

  outputs = { nixpkgs, ... }: let
    system = "x86_64-linux";
    pkgs = import nixpkgs { inherit system; };
  in {
    devShells.${system}.default =
      (pkgs.buildFHSEnv {
        name = "android-dev";

        targetPkgs = pkgs: with pkgs; [
          zsh
          jdk17
          android-tools

          go

          zlib
          stdenv.cc.cc.lib
        ];

        profile = ''
          export JAVA_HOME="${pkgs.jdk17.home}"

          export ANDROID_HOME="$HOME/.android/Sdk"
          export ANDROID_SDK_ROOT="$ANDROID_HOME"

          export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"
        '';

        runScript = "zsh";
      }).env;
  };
}

{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    treefmt-nix = {
      url = "github:numtide/treefmt-nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, treefmt-nix }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs {
        inherit system;
      };
      treefmtEval = treefmt-nix.lib.evalModule pkgs ./treefmt.nix;

      idea = pkgs.jetbrains.idea-community-bin;
      jdk = pkgs.jdk22;
    in
    {
      formatter.${system} = treefmtEval.config.build.wrapper;
      checks.${system}.formatter = treefmtEval.config.build.check self;

      devShells.${system}.default = pkgs.mkShell {
        packages = with pkgs; [
          quarkus
          detekt
          jdk
          texlive.combined.scheme-full
        ];
      };

      packages.${system} = {
        idea-local = pkgs.writeShellScriptBin "texerer-idea-local" ''
          export JAVA_HOME="${jdk.home}"
          idea-community . >/dev/null 2>&1 & "''${@:1}"
        '';
        idea = pkgs.writeShellScriptBin "texerer-idea" ''
          export JAVA_HOME="${jdk.home}"
          ${idea}/bin/idea-community . >/dev/null 2>&1 & "''${@:1}"
        '';
      };
    };
}

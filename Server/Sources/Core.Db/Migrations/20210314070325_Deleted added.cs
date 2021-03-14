using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Deletedadded : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "Deleted",
                schema: "citylogia",
                table: "Users",
                type: "boolean",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "Deleted",
                schema: "citylogia",
                table: "Reviews",
                type: "boolean",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "Deleted",
                schema: "citylogia",
                table: "Places-Types",
                type: "boolean",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "Deleted",
                schema: "citylogia",
                table: "Places",
                type: "boolean",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "Deleted",
                schema: "citylogia",
                table: "Photos",
                type: "boolean",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "Deleted",
                schema: "citylogia",
                table: "Address",
                type: "boolean",
                nullable: false,
                defaultValue: false);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Deleted",
                schema: "citylogia",
                table: "Users");

            migrationBuilder.DropColumn(
                name: "Deleted",
                schema: "citylogia",
                table: "Reviews");

            migrationBuilder.DropColumn(
                name: "Deleted",
                schema: "citylogia",
                table: "Places-Types");

            migrationBuilder.DropColumn(
                name: "Deleted",
                schema: "citylogia",
                table: "Places");

            migrationBuilder.DropColumn(
                name: "Deleted",
                schema: "citylogia",
                table: "Photos");

            migrationBuilder.DropColumn(
                name: "Deleted",
                schema: "citylogia",
                table: "Address");
        }
    }
}
